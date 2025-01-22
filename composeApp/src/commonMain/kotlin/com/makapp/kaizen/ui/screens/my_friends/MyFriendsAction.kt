package com.makapp.kaizen.ui.screens.my_friends

import com.makapp.kaizen.domain.models.FriendRequest

sealed class MyFriendsAction {
    data class OnFriendUsernameInputChanged(val username: String) : MyFriendsAction()
    data object OnSearchFriendProfile : MyFriendsAction()
    data object OnFriendRequestSubmit : MyFriendsAction()
    data class OnFriendRequestUpdated(val requestId: String, val status: FriendRequest.Status) : MyFriendsAction()
    data object OnNavigateUp : MyFriendsAction()
}