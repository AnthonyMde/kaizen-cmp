package org.towny.kaizen.ui.screens.home

import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.models.UserSession

data class HomeScreenState(
    val userSession: UserSession? = null,

    val currentChallenger: User? = null,
    val isCurrentChallengerLoading: Boolean = false,
    val currentChallengerError: String? = null,

    val friends: List<User> = emptyList(),
    val isFriendsLoading: Boolean = false,
    val friendsError: String? = null,
)
