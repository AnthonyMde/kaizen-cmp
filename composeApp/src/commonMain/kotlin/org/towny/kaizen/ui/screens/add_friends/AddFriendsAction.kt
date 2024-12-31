package org.towny.kaizen.ui.screens.add_friends

sealed class AddFriendsAction {
    data class OnFriendIdInputChanged(val text: String) : AddFriendsAction()
    data object OnNavigateUp : AddFriendsAction()
}