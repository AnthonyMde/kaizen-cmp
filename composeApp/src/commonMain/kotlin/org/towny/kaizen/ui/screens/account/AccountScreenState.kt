package org.towny.kaizen.ui.screens.account

import org.towny.kaizen.domain.models.User

data class AccountScreenState(
    val isLogoutLoading: Boolean = false,
    val showLogoutConfirmationModal: Boolean = false,
    val isDeleteUserAccountLoading: Boolean = false,
    val showDeleteUserAccountConfirmationModal: Boolean = false,
    val deleteUserAccountError: String? = null,
    val user: User? = null
)