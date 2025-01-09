package org.towny.kaizen.ui.screens.my_friends

sealed class MyFriendsAction {
    data class OnFriendUsernameInputChanged(val username: String) : MyFriendsAction()
    data object OnMyFriendFormSubmit : MyFriendsAction()
    data object OnNavigateUp : MyFriendsAction()
}