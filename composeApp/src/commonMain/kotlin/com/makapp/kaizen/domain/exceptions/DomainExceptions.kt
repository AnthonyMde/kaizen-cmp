package com.makapp.kaizen.domain.exceptions

sealed class DomainException(message: String = "") : Exception(message) {
    sealed class Common : DomainException() {
        data object Unknown : Common()
        data object InvalidArguments : Common()
        data object NotFound : Common()
        data object ServerInternalError : Common()
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

    sealed class Friend : DomainException() {
        data object CannotSearchForYourself : Friend()
        data object CannotSendFriendRequestToYourself : Friend()
    }

    sealed class Challenge : DomainException() {
        data object HasNoName : Challenge()
        data object HasNoMaxErrors : Challenge()
    }
}
