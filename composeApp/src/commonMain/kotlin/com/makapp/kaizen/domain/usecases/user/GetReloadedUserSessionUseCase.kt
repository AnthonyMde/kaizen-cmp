package com.makapp.kaizen.domain.usecases.user

import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.user.UserSession
import com.makapp.kaizen.domain.repository.AuthRepository

class GetReloadedUserSessionUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): UserSession? {
        return try {
            authRepository.reloadUserSession()
        } catch (e: Exception) {
            if (e !is DomainException.Common.NoNetworkError) {
                authRepository.logout()
            }

            null
        }
    }
}
