package org.towny.kaizen.ui.screens.account

import org.towny.kaizen.domain.models.User

data class AccountScreenState(
    val isLogoutLoading: Boolean = false,
    val user: User? = null
)