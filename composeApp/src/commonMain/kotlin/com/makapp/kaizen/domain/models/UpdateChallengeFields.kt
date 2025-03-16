package com.makapp.kaizen.domain.models

import com.makapp.kaizen.domain.models.Challenge.Status

data class UpdateChallengeFields(
    val name: String? = null,
    val status: Status? = null,
    val maxAuthorizedFailures: Int? = null,
    val commitment: String? = null,
    val expectations: String? = null,
    val isDeleted: Boolean? = null,
)
