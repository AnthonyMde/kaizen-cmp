package com.makapp.kaizen.domain.models.user

import com.makapp.kaizen.domain.models.challenge.Challenge
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val displayName: String? = null,
    val profilePictureIndex: Int,
    val challenges: List<Challenge>
) {
    fun getUsername() = if (!displayName.isNullOrBlank())
        displayName
    else
        name
}
