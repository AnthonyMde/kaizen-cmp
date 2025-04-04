package com.makapp.kaizen.domain.exceptions

sealed class DomainException(message: String? = null) : Exception(message) {
    sealed class Common(override val message: String? = null) : DomainException(message = message) {
        data class Unknown(override val message: String? = null) : Common(message = message)
        data object InvalidArguments : Common()
        data object NotFound : Common()
        data object ServerInternalError : Common()
        data object NoNetworkError : Common()
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
            data object IncorrectLength : DisplayName()
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
