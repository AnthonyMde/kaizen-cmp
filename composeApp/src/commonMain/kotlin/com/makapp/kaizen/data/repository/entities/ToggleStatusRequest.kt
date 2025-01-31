package com.makapp.kaizen.data.repository.entities

import dev.gitlive.firebase.firestore.Timestamp

data class ToggleStatusRequest(
    val userId: String,
    val challengeId: String,
    val isChecked: Boolean,
    val updatedAt: Timestamp
)
