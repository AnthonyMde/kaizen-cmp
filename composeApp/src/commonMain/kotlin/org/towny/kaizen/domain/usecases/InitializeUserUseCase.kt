package org.towny.kaizen.domain.usecases

import dev.gitlive.firebase.auth.FirebaseUser
import org.towny.kaizen.domain.repository.AuthRepository

class InitializeUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): FirebaseUser? {
        var user = authRepository.getUserSession()
        try {
            user?.reload()
            user = authRepository.getUserSession()
        } catch (e: Exception) {
            authRepository.logout()
        } finally {
            return user
        }
    }
}