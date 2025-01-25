package com.makapp.kaizen.ui.screens.home

import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.User
import com.makapp.kaizen.domain.models.UserSession

data class HomeScreenState(
    val userSession: UserSession? = null,

    val isCurrentChallengerLoading: Boolean = true,
    val currentChallenger: User? = null,
    val currentChallengerError: String? = null,

    val friends: List<Friend> = emptyList(),
    val isFriendsLoading: Boolean = false,
    val friendsError: String? = null,
)
