package com.makapp.kaizen.domain.usecases

import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource

class VerifyUsernameFormatUseCase {
    companion object {
        private const val USERNAME_LENGTH = "^.{1,30}\$"
        private const val USERNAME_AUTHORIZED_CHAR = "^[a-zA-Z0-9._]*\$"
        private const val USERNAME_NO_CONSECUTIVE_SPE_CHAR = "^(?!.*[_.]{2}).*\$"
        private const val USERNAME_NO_SPE_CHAR_AT_START_OR_END = "^(?![._]{1}).*(?<![_.])\$"
    }

    operator fun invoke(username: String?): Resource<Unit> {
        val error = if (username == null || username.trim().isBlank()) {
            DomainException.User.Username.IsEmpty
        } else if (!isLengthValid(username)) {
            DomainException.User.Username.IncorrectLength
        } else if (!isUsernameCharsAuthorized(username)) {
            DomainException.User.Username.SpecialCharNotAuthorized
        } else if (!hasNoConsecutiveSpeChar(username)) {
            DomainException.User.Username.DoubleSpecialCharNotAuthorized
        } else if (!hasNoSpeCharAtEndOrStart(username)) {
            DomainException.User.Username.SpecialCharAtStartOrEndNotAuthorized
        } else {
            null
        }

        return error?.let { Resource.Error(it) } ?: Resource.Success()
    }

    private fun isLengthValid(username: String): Boolean = Regex(USERNAME_LENGTH).matches(username)
    private fun isUsernameCharsAuthorized(username: String): Boolean = Regex(
        USERNAME_AUTHORIZED_CHAR
    ).matches(username)

    private fun hasNoConsecutiveSpeChar(username: String): Boolean = Regex(
        USERNAME_NO_CONSECUTIVE_SPE_CHAR
    ).matches(username)

    private fun hasNoSpeCharAtEndOrStart(username: String): Boolean = Regex(
        USERNAME_NO_SPE_CHAR_AT_START_OR_END
    ).matches(username)
}