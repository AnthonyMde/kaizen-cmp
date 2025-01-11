package org.towny.kaizen.domain.exceptions

sealed class DomainException(message: String = "") : Exception(message) {
    sealed class Auth : DomainException() {
        data object UserNotAuthorized : Auth()
        data object PasswordIsEmpty : Auth()
        data object WeakPassword : Auth()
        data object InvalidCredentials : Auth()
        data object EmailAddressAlreadyUsed : Auth()
        data object FailedToSendEmailVerification : Auth()
    }

    sealed class User : DomainException() {
        data object NoUserSessionFound : User()
        data object NoUserAccountFound : User()

        sealed class Name : User() {
            data object CannotBeVerified : Name()
            data object AlreadyUsed : Name()
            data object IsEmpty : Name()
            data object IncorrectLength : Name()
            data object SpecialCharAtStartOrEndNotAuthorized : Name()
            data object DoubleSpecialCharNotAuthorized : Name()
            data object SpecialCharNotAuthorized : Name()
        }
    }

    sealed class Challenge : DomainException() {
        data object HasNoName : Challenge()
        data object HasNoMaxErrors : Challenge()
    }
}
