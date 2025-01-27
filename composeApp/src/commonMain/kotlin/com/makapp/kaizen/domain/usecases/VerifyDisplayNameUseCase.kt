package com.makapp.kaizen.domain.usecases

import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource

class VerifyDisplayNameUseCase {
    companion object {
        private const val MAXIMUM_DISPLAY_NAME_LENGTH = 30
    }

    operator fun invoke(displayName: String): Resource<Unit> =
        if (displayName.isBlank()) {
            Resource.Error(DomainException.User.DisplayName.IsEmpty)
        } else if (displayName.length > MAXIMUM_DISPLAY_NAME_LENGTH) {
            Resource.Error(DomainException.User.DisplayName.IncorrectLength)
        } else {
            Resource.Success()
        }
}
