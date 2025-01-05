package org.towny.kaizen.domain.usecases

import org.towny.kaizen.domain.models.UserSession
import org.towny.kaizen.domain.repository.AuthRepository

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
