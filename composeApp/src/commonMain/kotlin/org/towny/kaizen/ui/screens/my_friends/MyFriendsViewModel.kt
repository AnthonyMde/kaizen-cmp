package org.towny.kaizen.ui.screens.my_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

            MyFriendsAction.OnMyFriendFormSubmit -> viewModelScope.launch {
                createFriendRequest()
            }

            else -> {}
        }
    }

    private suspend fun createFriendRequest() {
        val username = _myFriendsState.value.friendUsernameInputValue.trim()
        if (!requiredField(username)) return

        _myFriendsState.update {
            it.copy(
                isFormSubmissionLoading = true,
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
        _myFriendsState.update { it.copy(isFormSubmissionLoading = false) }
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