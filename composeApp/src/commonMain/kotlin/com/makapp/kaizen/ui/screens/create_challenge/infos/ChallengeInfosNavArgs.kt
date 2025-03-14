package com.makapp.kaizen.ui.screens.create_challenge.infos

data class ChallengeInfosNavArgs(
    val isEditing: Boolean,
    val title: String?,
    val lives: Int?,
    val challengeId: String?
)