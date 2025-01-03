package org.towny.kaizen.domain.usecases

import dev.gitlive.firebase.auth.FirebaseUser
import org.towny.kaizen.domain.repository.AuthRepository

class ReloadUserSessionUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): FirebaseUser? {
        val user = authRepository.getUserSession()
        user?.reload()
        return authRepository.getUserSession()
    }
}