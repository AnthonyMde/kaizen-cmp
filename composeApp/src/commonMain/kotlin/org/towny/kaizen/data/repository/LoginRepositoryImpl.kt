package org.towny.kaizen.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.data.local.LocalPreferencesDataSourceImpl
import org.towny.kaizen.data.remote.FirestoreDataSource
import org.towny.kaizen.data.repository.sources.LocalPreferencesDataSource
import org.towny.kaizen.data.repository.sources.RemoteDataSource
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val remoteDataSource: RemoteDataSource = FirestoreDataSource(),
    private val preferencesDataSource: LocalPreferencesDataSource = LocalPreferencesDataSourceImpl()
) : LoginRepository {

    override fun login(username: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val user = remoteDataSource.getUserBy(username)
        if (user != null) {
            preferencesDataSource.saveUser(user.toUser())
            emit(Resource.Success())
        } else {
            emit(Resource.Error(DomainException.Login.UserNotAuthorized))
        }
    }

}