package org.towny.kaizen.ui.screens.add_friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.services.FriendsService

class AddFriendsViewModel(
    private val friendsService: FriendsService,
) : ViewModel() {
    private val _addFriendsState = MutableStateFlow(AddFriendsState())
    val addFriendsState = _addFriendsState.asStateFlow()

    fun onAction(action: AddFriendsAction) {
        when (action) {
            is AddFriendsAction.OnFriendUsernameInputChanged -> {
                _addFriendsState.update {
                    it.copy(
                        friendUsernameInputValue = action.username,
                        friendUsernameInputError = null
                    )
                }
            }

            AddFriendsAction.OnAddFriendFormSubmit -> viewModelScope.launch {
                createFriendRequest()
            }

            else -> {}
        }
    }

    private suspend fun createFriendRequest() {
        val username = _addFriendsState.value.friendUsernameInputValue.trim()
        if (!requiredField(username)) return

        _addFriendsState.update {
            it.copy(
                isFormSubmissionLoading = true,
                friendUsernameInputError = null
            )
        }

        friendsService.createFriendRequest(friendUsername = username).let { result ->
            when (result) {
                is Resource.Error -> {
                    _addFriendsState.update { it.copy(friendUsernameInputError = result.throwable?.message) }
                }

                is Resource.Success -> {
                    println("DEBUG: (AddFriendsViewModel) FriendRequest was created successfully")
                }

                is Resource.Loading -> {}
            }
        }
        _addFriendsState.update { it.copy(isFormSubmissionLoading = false) }
    }

    private fun requiredField(username: String): Boolean {
        val errorMessage = when {
            username.isBlank() -> "Username should be empty"
            username.length < 2 -> "A username contains at least 2 characters."
            else -> null
        }

        _addFriendsState.update { it.copy(friendUsernameInputError = errorMessage) }

        return errorMessage == null
    }
}