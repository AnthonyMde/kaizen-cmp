package org.towny.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.AuthSuccess
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.AuthRepository

class AuthService(
    private val authRepository: AuthRepository
) {
    fun authenticate(email: String, password: String): Flow<Resource<AuthSuccess>> = flow {
        emit(Resource.Loading())

        try {
            authRepository.signUp(email, password)
            val userSession = authRepository.getUserSession()
            if (userSession != null) {
                authRepository.sendEmailVerification()
                emit(Resource.Success(AuthSuccess(hasSendVerificationEmail = true)))
            } else {
                emit(Resource.Error(DomainException.Auth.FailedToSendEmailVerification))
            }
        } catch (e: Exception) {
            when (e) {
                is DomainException.Auth.WeakPassword -> {
                    emit(Resource.Error(e))
                }

                else -> {
                    login(email, password)
                }
            }
        }
    }

    private suspend fun FlowCollector<Resource<AuthSuccess>>.login(
        email: String,
        password: String
    ) {
        try {
            authRepository.signIn(email, password)
            emit(Resource.Success())
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}