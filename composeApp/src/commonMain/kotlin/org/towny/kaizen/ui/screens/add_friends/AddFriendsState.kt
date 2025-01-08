package org.towny.kaizen.ui.screens.add_friends

data class AddFriendsState(
    val friendUsernameInputValue: String = "",
    val friendUsernameInputError: String? = null,
    val isFormSubmissionLoading: Boolean = false
)
