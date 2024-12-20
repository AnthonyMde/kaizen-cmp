package org.towny.kaizen.domain.exceptions

sealed class DomainException(message: String = ""): Exception(message) {
    data object NoSavedUserFound: DomainException()

    sealed class Login: DomainException() {
        data object UserNotAuthorized: Login()
        data object PasswordIsEmpty: Login()
    }
}
