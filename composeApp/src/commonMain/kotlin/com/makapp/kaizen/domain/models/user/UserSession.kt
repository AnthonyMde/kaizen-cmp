package com.makapp.kaizen.domain.models.user

data class UserSession(
    val uid: String,
    val email: String,
    val isEmailVerified: Boolean
)
