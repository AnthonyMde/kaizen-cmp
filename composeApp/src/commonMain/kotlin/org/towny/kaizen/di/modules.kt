package org.towny.kaizen.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.towny.kaizen.data.local.LocalPreferencesDataSourceImpl
import org.towny.kaizen.data.remote.RemoteFirestoreDataSourceImpl
import org.towny.kaizen.data.repository.ChallengesRepositoryImpl
import org.towny.kaizen.data.repository.LoginRepositoryImpl
import org.towny.kaizen.data.repository.UsersRepositoryImpl
import org.towny.kaizen.data.repository.sources.LocalPreferencesDataSource
import org.towny.kaizen.data.repository.sources.RemoteFirestoreDataSource
import org.towny.kaizen.domain.repository.ChallengesRepository
import org.towny.kaizen.domain.repository.LoginRepository
import org.towny.kaizen.domain.repository.UsersRepository
import org.towny.kaizen.domain.services.ChallengesService
import org.towny.kaizen.domain.services.GetUserSessionUseCase
import org.towny.kaizen.domain.services.LoginService
import org.towny.kaizen.ui.screens.home.HomeViewModel
import org.towny.kaizen.ui.screens.login.LoginViewModel

val commonModules = module {
    // ViewModels
    viewModelOf(::HomeViewModel)
    viewModelOf(::LoginViewModel)

    // Service
    singleOf(::LoginService)
    singleOf(::ChallengesService)

    // Use cases
    singleOf(::GetUserSessionUseCase)

    // Repository
    singleOf(::UsersRepositoryImpl).bind<UsersRepository>()
    singleOf(::ChallengesRepositoryImpl).bind<ChallengesRepository>()
    singleOf(::LoginRepositoryImpl).bind<LoginRepository>()

    // Data sources
    singleOf(::LocalPreferencesDataSourceImpl).bind<LocalPreferencesDataSource>()
    singleOf(::RemoteFirestoreDataSourceImpl).bind<RemoteFirestoreDataSource>()
}

expect val targetModule: Module