package org.towny.kaizen.domain.models

data class UserSession(
    val uid: String,
    val email: String,
    val isEmailVerified: Boolean
)
