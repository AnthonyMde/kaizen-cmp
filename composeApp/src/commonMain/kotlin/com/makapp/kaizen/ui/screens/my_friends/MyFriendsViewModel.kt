package com.makapp.kaizen.ui.screens.my_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.services.FriendRequestsService
import com.makapp.kaizen.domain.services.FriendsService
import com.makapp.kaizen.domain.services.UsersService
import com.makapp.kaizen.domain.usecases.GetFriendPreviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyFriendsViewModel(
    private val friendRequestsService: FriendRequestsService,
    private val getFriendPreviewUseCase: GetFriendPreviewUseCase,
    private val friendsService: FriendsService,
    private val usersService: UsersService
) : ViewModel() {
    private val _myFriendsState = MutableStateFlow(MyFriendsState())
    val myFriendsState = _myFriendsState.asStateFlow()
        .onStart {
            viewModelScope.launch { friendsService.refreshFriendPreviews() }
            viewModelScope.launch { friendRequestsService.refreshFriendRequests() }
            watchFriendRequestsRefreshing()
            watchFriendRequests()
            watchFriendPreviewsRefreshing()
            watchFriendPreviews()
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

            is MyFriendsAction.OnFriendRequestUpdated -> viewModelScope.launch {
                updateFriendRequest(action)
            }

            else -> {}
        }
    }

    private fun watchFriendRequestsRefreshing() = viewModelScope.launch {
        friendRequestsService.isFriendRequestsRefreshing.collectLatest { isRefreshing ->
            _myFriendsState.update {
                it.copy(
                    areFriendRequestsLoading = isRefreshing
                )
            }
        }
    }

    private fun watchFriendRequests() = viewModelScope.launch {
        usersService.getMe().let { user ->
            friendRequestsService.watchFriendRequests().collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        // TODO
                    }

                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        val sent = result.data?.filter { it.sender.id == user!!.id } ?: emptyList()
                        val received =
                            result.data?.filter { it.receiver.id == user!!.id } ?: emptyList()
                        _myFriendsState.update {
                            it.copy(
                                pendingSentRequests = sent,
                                pendingReceivedRequests = received
                            )
                        }
                    }
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

        friendRequestsService.createFriendRequest(friendId).let { result ->
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
                    _myFriendsState.update {
                        it.copy(
                            friendPreview = null,
                            friendUsernameInputValue = ""
                        )
                    }
                }

                is Resource.Loading -> {}
            }
        }
        _myFriendsState.update { it.copy(isSendFriendRequestLoading = false) }
    }

    private suspend fun updateFriendRequest(action: MyFriendsAction.OnFriendRequestUpdated) {
        friendRequestsService.updateFriendRequest(action.requestId, action.status)
            .collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        // TODO: Add toast showing error
                        _myFriendsState.update {
                            it.copy(
                                requestIdsCurrentlyUpdated =
                                getNewRequestIdsUnderUpdate(
                                    _myFriendsState.value.requestIdsCurrentlyUpdated,
                                    action.requestId
                                )
                            )
                        }
                    }

                    is Resource.Success -> {
                        _myFriendsState.update {
                            it.copy(
                                requestIdsCurrentlyUpdated =
                                getNewRequestIdsUnderUpdate(
                                    _myFriendsState.value.requestIdsCurrentlyUpdated,
                                    action.requestId
                                )
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _myFriendsState.update {
                            it.copy(
                                requestIdsCurrentlyUpdated = getNewRequestIdsUnderUpdate(
                                    _myFriendsState.value.requestIdsCurrentlyUpdated,
                                    action.requestId,
                                    true
                                )
                            )
                        }
                    }
                }
            }
    }

    private fun getNewRequestIdsUnderUpdate(
        entry: List<String>,
        id: String,
        adding: Boolean = false
    ): List<String> {
        val list = entry.toMutableList()
        if (adding) {
            list.add(id)
        } else {
            list.remove(id)
        }
        return list
    }

    private fun watchFriendPreviews() = viewModelScope.launch {
        friendsService.watchFriendPreviews().collectLatest { result ->
            when (result) {
                is Resource.Error -> {
                    _myFriendsState.update {
                        it.copy(
                            friendPreviews = result.data ?: emptyList(),
                        )
                    }
                }

                is Resource.Success -> {
                    _myFriendsState.update {
                        it.copy(
                            friendPreviews = result.data ?: emptyList(),
                        )
                    }
                }

                is Resource.Loading -> {}
            }
        }
    }

    private fun watchFriendPreviewsRefreshing() = viewModelScope.launch {
        friendsService.isRefreshingPreviews.collectLatest { isRefreshing ->
            _myFriendsState.update {
                it.copy(
                    areFriendPreviewsLoading = isRefreshing
                )
            }
        }
    }

    private suspend fun getFriendPreview(username: String) {
        getFriendPreviewUseCase(username).collectLatest { result ->
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
}
