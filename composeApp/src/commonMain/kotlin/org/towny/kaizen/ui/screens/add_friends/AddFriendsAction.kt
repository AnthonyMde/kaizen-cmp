package org.towny.kaizen.ui.screens.add_friends

sealed class AddFriendsAction {
    data class OnFriendUsernameInputChanged(val username: String) : AddFriendsAction()
    data object OnAddFriendFormSubmit : AddFriendsAction()
    data object OnNavigateUp : AddFriendsAction()
}