package org.towny.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.towny.kaizen.data.repository.LoginRepositoryImpl
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.LoginRepository

class LoginService(
    private val loginRepository: LoginRepository = LoginRepositoryImpl()
) {
    fun login(username: String): Flow<Resource<Unit>> {
        if (username.isBlank()) {
            return flowOf(Resource.Error(DomainException.Login.PasswordIsEmpty))
        }

        return loginRepository.login(username)
    }
}