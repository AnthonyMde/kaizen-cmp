package org.towny.kaizen.ui.screens.my_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.UsersRepository
import org.towny.kaizen.domain.services.FriendsService

class MyFriendsViewModel(
    private val friendsService: FriendsService,
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val _myFriendsState = MutableStateFlow(MyFriendsState())
    val myFriendsState = _myFriendsState.asStateFlow()
        .onStart {
            getFriendRequests()
        }

    private suspend fun getFriendRequests() {
        usersRepository.getCurrentUser().let { user ->
            try {
                friendsService.getFriendRequests().data?.let { requests ->
                    val sent = requests.filter { it.sender.id == user!!.id }
                    val received = requests.filter { it.receiver.id == user!!.id }
                    _myFriendsState.update {
                        it.copy(
                            pendingSentRequests = sent,
                            pendingReceivedRequests = received,
                            areFriendRequestsLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _myFriendsState.update { it.copy(areFriendRequestsLoading = false) }
            }
        }
    }

    fun onAction(action: MyFriendsAction) {
        when (action) {
            is MyFriendsAction.OnFriendUsernameInputChanged -> {
                _myFriendsState.update {
                    it.copy(
                        friendUsernameInputValue = action.username,
                        friendUsernameInputError = null
                    )
                }
            }

            is MyFriendsAction.OnSearchFriendProfile -> viewModelScope.launch {
                val username = _myFriendsState.value.friendUsernameInputValue
                getFriendPreview(username)
            }

            is MyFriendsAction.OnFriendRequestSubmit -> viewModelScope.launch {
                createFriendRequest()
            }

            else -> {}
        }
    }

    private suspend fun getFriendPreview(username: String) {
        friendsService.getFriendPreview(username).collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    val errorMessage = when (result.throwable) {
                        DomainException.Common.NotFound -> "No user is matching this username."
                        DomainException.Auth.UserNotAuthenticated -> "You are not authenticated."
                        DomainException.Common.InvalidArguments -> "Username should not be empty."
                        DomainException.Friend.CannotSearchForYourself -> "You cannot send friend request to yourself."
                        else -> "Something went wrong. Please, verify this username is valid or contact us."
                    }
                    _myFriendsState.update {
                        it.copy(
                            friendUsernameInputError = errorMessage,
                            friendPreview = null,
                            isFriendPreviewLoading = false
                        )
                    }
                }

                is Resource.Success -> {
                    _myFriendsState.update {
                        it.copy(
                            friendPreview = result.data,
                            isFriendPreviewLoading = false
                        )
                    }
                }

                is Resource.Loading -> {
                    _myFriendsState.update { it.copy(isFriendPreviewLoading = true) }
                }
            }
        }
    }

    private suspend fun createFriendRequest() {
        val friendId = _myFriendsState.value.friendPreview?.id

        _myFriendsState.update {
            it.copy(
                isSendFriendRequestLoading = true,
                friendUsernameInputError = null
            )
        }

        friendsService.createFriendRequest(friendId).let { result ->
            when (result) {
                is Resource.Error -> {
                    val errorMessage = when (result.throwable) {
                        DomainException.Auth.UserNotAuthenticated -> "You are not authenticated."
                        DomainException.Common.InvalidArguments -> "Cannot find friend id."
                        else -> "Impossible to send your friend request."
                    }
                    _myFriendsState.update { it.copy(friendUsernameInputError = errorMessage) }
                }

                is Resource.Success -> {
                    getFriendRequests()
                    _myFriendsState.update { it.copy(friendPreview = null) }
                }

                is Resource.Loading -> {}
            }
        }
        _myFriendsState.update { it.copy(isSendFriendRequestLoading = false) }
    }
}
