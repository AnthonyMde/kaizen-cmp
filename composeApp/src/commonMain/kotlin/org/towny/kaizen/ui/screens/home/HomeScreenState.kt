package org.towny.kaizen.ui.screens.home

import dev.gitlive.firebase.auth.FirebaseUser
import org.towny.kaizen.domain.models.User

data class HomeScreenState(
    val isLoading: Boolean = false,
    val currentChallenger: User? = null,
    val otherChallengers: List<User> = emptyList(),
    val error: String? = null,
    val user: FirebaseUser? = null
)
