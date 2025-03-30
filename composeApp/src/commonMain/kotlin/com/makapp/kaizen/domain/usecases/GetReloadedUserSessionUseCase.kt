package com.makapp.kaizen.domain.usecases

import com.makapp.kaizen.domain.models.user.UserSession
import com.makapp.kaizen.domain.repository.AuthRepository

class GetReloadedUserSessionUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): UserSession? {
        return try {
            authRepository.reloadUserSession()
        } catch (e: Exception) {
            authRepository.logout()
            null
        }
    }
}
