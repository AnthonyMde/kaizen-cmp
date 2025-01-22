package com.makapp.kaizen.domain.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.AuthSuccess
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.AuthRepository

class AuthenticateService(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(email: String, password: String): Flow<Resource<AuthSuccess>> =
        flow {
            emit(Resource.Loading())

            val signUpResult = authRepository.signUp(email, password)

            if (signUpResult is Resource.Error) {
                when (signUpResult.throwable) {
                    // Try to login when account already exists.
                    is DomainException.Auth.EmailAddressAlreadyUsed -> {
                        emit(login(email, password))
                    }

                    else -> {
                        emit(Resource.Error(signUpResult.throwable))
                        return@flow
                    }
                }
            }

            if (signUpResult is Resource.Success) {
                emit(sendVerificationEmail())
            }
        }

    private suspend fun login(
        email: String,
        password: String
    ): Resource<AuthSuccess> {
        val signInResult = authRepository.signIn(email, password)
        return if (signInResult is Resource.Error) {
            Resource.Error(signInResult.throwable)
        } else {
            Resource.Success(AuthSuccess(isSignUp = false))
        }
    }

    private suspend fun sendVerificationEmail(): Resource<AuthSuccess> {
        val userSession = authRepository.getUserSession()
        return if (userSession != null) {
            authRepository.sendVerificationEmail()
            Resource.Success(AuthSuccess(isSignUp = true))
        } else {
            Resource.Error(DomainException.Auth.FailedToSendEmailVerification)
        }
    }
}