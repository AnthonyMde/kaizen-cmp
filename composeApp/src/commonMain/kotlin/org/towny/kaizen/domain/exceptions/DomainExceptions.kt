package org.towny.kaizen.domain.exceptions

sealed class DomainException(message: String = "") : Exception(message) {
    sealed class Login : DomainException() {
        data object UserNotAuthorized : Login()
        data object PasswordIsEmpty : Login()
    }

    sealed class User : DomainException() {
        data object NoUserSessionFound : User()
    }

    sealed class Challenge : DomainException() {
        data object HasNoName : Challenge()
        data object HasNoMaxErrors : Challenge()
    }
}
