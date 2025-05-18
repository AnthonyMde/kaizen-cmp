package com.makapp.kaizen.domain.usecases

import com.makapp.kaizen.domain.FlowHelper.mapSuccess
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.challenge.Challenge
import com.makapp.kaizen.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow

class ObserveArchivedKaizensUseCase(
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(): Flow<Resource<List<Challenge>>> {
        return usersRepository.watchCurrentUser().mapSuccess { user ->
            user ?: return@mapSuccess emptyList()
            user.challenges.filter {
                it.isAbandoned() || it.isFailed()
            }
        }
    }
}