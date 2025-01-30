package com.makapp.kaizen.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val email: String,
    val name: String,
    val displayName: String? = null,
    val profilePictureIndex: Int,
)
