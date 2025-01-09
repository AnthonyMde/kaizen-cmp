package org.towny.kaizen.ui.screens.my_friends

data class MyFriendsState(
    val friendUsernameInputValue: String = "",
    val friendUsernameInputError: String? = null,
    val isFormSubmissionLoading: Boolean = false
)
