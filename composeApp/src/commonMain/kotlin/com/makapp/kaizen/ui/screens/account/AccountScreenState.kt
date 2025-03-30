package com.makapp.kaizen.ui.screens.account

import com.makapp.kaizen.domain.models.user.User
import org.jetbrains.compose.resources.StringResource

data class AccountScreenState(
    val isLogoutLoading: Boolean = false,
    val showLogoutConfirmationModal: Boolean = false,
    val isDeleteUserAccountLoading: Boolean = false,
    val showDeleteUserAccountConfirmationModal: Boolean = false,
    val showDeleteFinalConfirmationModal: Boolean = false,
    val deleteUserAccountError: StringResource? = null,
    val user: User? = null
)