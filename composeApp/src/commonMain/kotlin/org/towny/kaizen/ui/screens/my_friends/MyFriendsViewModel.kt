package org.towny.kaizen.ui.screens.my_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.services.FriendsService

class MyFriendsViewModel(
    private val friendsService: FriendsService,
) : ViewModel() {
    private val _myFriendsState = MutableStateFlow(MyFriendsState())
    val myFriendsState = _myFriendsState.asStateFlow()

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
        _myFriendsState.update { it.copy(isFriendPreviewLoading = true) }
        friendsService.getFriendPreview(username).let { result ->
            when (result) {
                is Resource.Error -> {
                    val errorMessage = when (result.throwable) {
                        DomainException.Common.NotFound -> "No user is matching this username."
                        DomainException.Auth.UserNotAuthenticated -> "You are not authenticated."
                        DomainException.Common.InvalidArguments -> "Username should not be empty."
                        DomainException.Common.Unknown -> "An unknown error occurred."
                        else -> result.throwable?.message ?: "Something went wrong."
                    }
                    _myFriendsState.update {
                        it.copy(
                            friendUsernameInputError = errorMessage,
                            friendPreview = null
                        )
                    }
                }

                is Resource.Success -> {
                    _myFriendsState.update { it.copy(friendPreview = result.data) }
                }

                else -> {}
            }
        }
        _myFriendsState.update { it.copy(isFriendPreviewLoading = false) }
    }

    private suspend fun createFriendRequest() {
        val username = _myFriendsState.value.friendUsernameInputValue.trim()
        if (!requiredField(username)) return

        _myFriendsState.update {
            it.copy(
                isFriendPreviewLoading = true,
                friendUsernameInputError = null
            )
        }

        friendsService.createFriendRequest(friendId = username).let { result ->
            when (result) {
                is Resource.Error -> {
                    _myFriendsState.update { it.copy(friendUsernameInputError = result.throwable?.message) }
                }

                is Resource.Success -> {
                    println("DEBUG: (MyFriendsViewModel) FriendRequest was created successfully")
                }

                is Resource.Loading -> {}
            }
        }
        _myFriendsState.update { it.copy(isFriendPreviewLoading = false) }
    }

    private fun requiredField(username: String): Boolean {
        val errorMessage = when {
            username.isBlank() -> "Username should be empty"
            username.length < 2 -> "A username contains at least 2 characters."
            else -> null
        }

        _myFriendsState.update { it.copy(friendUsernameInputError = errorMessage) }

        return errorMessage == null
    }
}