package com.makapp.kaizen.domain.usecases.auth

import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SendResetPasswordEmailUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        emit(authRepository.sendResetPasswordEmail(email))
    }
}
