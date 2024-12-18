package org.towny.kaizen.ui.screens.home

import org.towny.kaizen.domain.models.User

data class HomeScreenState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)