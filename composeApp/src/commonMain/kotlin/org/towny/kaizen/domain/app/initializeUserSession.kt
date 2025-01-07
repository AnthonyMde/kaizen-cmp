package org.towny.kaizen.domain.app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import org.towny.kaizen.domain.models.UserSession
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.usecases.GetReloadedUserSessionUseCase

fun initializeUserSession(): UserSession? {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val authRepository = KoinPlatform.getKoin().get<AuthRepository>()
    val getReloadedUserSessionUseCase = KoinPlatform.getKoin().get<GetReloadedUserSessionUseCase>()
    val session = authRepository.getUserSession()

    // Asynchronously reload firebase user cache.
    scope.launch {
        getReloadedUserSessionUseCase()
    }

    return session
}
