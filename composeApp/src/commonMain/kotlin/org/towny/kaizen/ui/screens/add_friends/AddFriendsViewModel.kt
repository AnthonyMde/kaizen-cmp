package org.towny.kaizen.ui.screens.add_friends

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddFriendsViewModel: ViewModel() {
    private val _addFriendsState = MutableStateFlow(AddFriendsState())
    val addFriendsState = _addFriendsState.asStateFlow()

    fun onAction(action: AddFriendsAction) {
        when (action) {
            is AddFriendsAction.OnFriendIdInputChanged -> {
                _addFriendsState.update {
                    it.copy(friendIdInputValue = action.text)
                }
            }

            else -> {}
        }
    }
}