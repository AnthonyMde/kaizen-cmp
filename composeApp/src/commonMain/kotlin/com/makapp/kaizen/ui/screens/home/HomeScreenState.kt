package com.makapp.kaizen.ui.screens.home

import com.makapp.kaizen.domain.models.friend.Friend
import com.makapp.kaizen.domain.models.user.User
import com.makapp.kaizen.domain.models.user.UserSession
import org.jetbrains.compose.resources.StringResource

data class HomeScreenState(
    val userSession: UserSession? = null,

    val isCurrentChallengerLoading: Boolean = true,
    val currentChallenger: User? = null,
    val currentChallengerError: StringResource? = null,

    val friends: List<Friend> = emptyList(),
    val isFriendsLoading: Boolean = false,
    val isSwipeToRefreshing: Boolean = false,
    val friendsError: StringResource? = null,
)
