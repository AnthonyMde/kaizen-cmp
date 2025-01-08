package org.towny.kaizen.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.AuthSuccess
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.AuthRepository

class AuthenticateUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<AuthSuccess>> =
        flow {
            emit(Resource.Loading())

            val signUpResult = authRepository.signUp(email, password)

            if (signUpResult is Resource.Error) {
                when (signUpResult.throwable) {
                    // Try to login when account already exists.
                    is DomainException.Auth.EmailAddressAlreadyUsed -> {
                        login(email, password)
                    }

                    else -> {
                        emit(Resource.Error(signUpResult.throwable))
                        return@flow
                    }
                }
            }

            if (signUpResult is Resource.Success) {
                sendVerificationEmail()
            }
        }

    private suspend fun FlowCollector<Resource<AuthSuccess>>.login(
        email: String,
        password: String
    ) {
        val signInResult = authRepository.signIn(email, password)
        if (signInResult is Resource.Error) {
            emit(Resource.Error(signInResult.throwable))
        } else {
            emit(Resource.Success(AuthSuccess(isSignUp = false)))
        }
    }

    private suspend fun FlowCollector<Resource<AuthSuccess>>.sendVerificationEmail() {
        val userSession = authRepository.getUserSession()
        if (userSession != null) {
            authRepository.sendVerificationEmail()
            emit(Resource.Success(AuthSuccess(isSignUp = true)))
        } else {
            emit(Resource.Error(DomainException.Auth.FailedToSendEmailVerification))
        }
    }
}