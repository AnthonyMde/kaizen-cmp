package org.towny.kaizen.domain.exceptions

sealed class DomainException(message: String = "") : Exception(message) {
    sealed class Auth : DomainException() {
        data object UserNotAuthorized : Auth()
        data object PasswordIsEmpty : Auth()
        data object WeakPassword : Auth()
        data object InvalidCredentials : Auth()
        data object EmailAddressAlreadyUsed: Auth()
        data object FailedToSendEmailVerification : Auth()
        data object UsernameCannotBeVerified : Auth()
    }

    sealed class User : DomainException() {
        data object NoUserSessionFound : User()
    }

    sealed class Challenge : DomainException() {
        data object HasNoName : Challenge()
        data object HasNoMaxErrors : Challenge()
    }
}
