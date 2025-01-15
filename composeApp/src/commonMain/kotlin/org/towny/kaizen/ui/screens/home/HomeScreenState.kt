package org.towny.kaizen.ui.screens.home

import org.towny.kaizen.domain.models.Friend
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.models.UserSession

data class HomeScreenState(
    val userSession: UserSession? = null,

    val currentChallenger: User? = null,
    val currentChallengerError: String? = null,

    val friends: List<Friend> = emptyList(),
    val isFriendsLoading: Boolean = false,
    val friendsError: String? = null,
)
