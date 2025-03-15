package com.makapp.kaizen.domain.exceptions

sealed class DomainException(message: String? = null) : Throwable(message) {
    sealed class Common(message: String? = null) : DomainException(message) {
        class Unknown(message: String? = null) : Common(message)
        class InvalidArguments(message: String? = null) : Common(message)
        class NotFound(message: String? = null) : Common(message)
        class ServerInternalError(message: String? = null) : Common(message)
    }

    sealed class Auth : DomainException() {
        data object UserNotAuthorized : Auth()
        data object UserNotAuthenticated : Auth()
        data object PasswordIsEmpty : Auth()
        data object WeakPassword : Auth()
        data object InvalidCredentials : Auth()
        data object EmailAddressAlreadyUsed : Auth()
        data object FailedToSendEmailVerification : Auth()
    }

    sealed class User : DomainException() {
        data object NoUserSessionFound : User()
        data object NoUserAccountFound : User()

        sealed class Username : User() {
            data object CannotBeVerified : Username()
            data object AlreadyUsed : Username()
            data object IsEmpty : Username()
            data object IncorrectLength : Username()
            data object SpecialCharAtStartOrEndNotAuthorized : Username()
            data object DoubleSpecialCharNotAuthorized : Username()
            data object SpecialCharNotAuthorized : Username()
        }

        sealed class DisplayName : User() {
            data object IncorrectLength: DisplayName()
        }
    }

    sealed class Friend : DomainException() {
        data object CannotSearchForYourself : Friend()
        data object CannotSendFriendRequestToYourself : Friend()
    }

    sealed class Challenge : DomainException() {
        data object HasNoName : Challenge()
    }
}
