package org.towny.kaizen.domain.exceptions

sealed class DomainException(message: String = ""): Exception(message) {

    sealed class Login: DomainException() {
        data object UserNotAuthorized: Login()
        data object PasswordIsEmpty: Login()
    }
}
