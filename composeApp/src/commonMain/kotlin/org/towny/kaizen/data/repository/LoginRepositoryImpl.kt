package org.towny.kaizen.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.data.repository.sources.LocalPreferencesDataSource
import org.towny.kaizen.data.repository.sources.RemoteFirestoreDataSource
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val remoteFirestoreDataSource: RemoteFirestoreDataSource,
    private val preferencesDataSource: LocalPreferencesDataSource
) : LoginRepository {

    override fun login(username: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val user = try {
            remoteFirestoreDataSource.getUserBy(username)
        } catch (e: Exception) {
            emit(Resource.Error(e))
            return@flow
        }
        if (user != null) {
            preferencesDataSource.saveUsername(user.name)
            emit(Resource.Success())
        } else {
            emit(Resource.Error(DomainException.Login.UserNotAuthorized))
        }
    }
}
