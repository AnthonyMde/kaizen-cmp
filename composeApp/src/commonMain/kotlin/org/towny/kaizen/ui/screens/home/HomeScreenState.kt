package org.towny.kaizen.ui.screens.home

import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.models.UserSession

data class HomeScreenState(
    val isLoading: Boolean = false,
    val currentChallenger: User? = null,
    val otherChallengers: List<User> = emptyList(),
    val error: String? = null,
    val userSession: UserSession? = null
)
