package org.towny.kaizen.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.towny.kaizen.data.local.LocalPreferencesDataSourceImpl
import org.towny.kaizen.data.remote.FirebaseAuth
import org.towny.kaizen.data.remote.RemoteFirestoreDataSourceImpl
import org.towny.kaizen.data.repository.ChallengesRepositoryImpl
import org.towny.kaizen.data.repository.AuthRepositoryImpl
import org.towny.kaizen.data.repository.UsersRepositoryImpl
import org.towny.kaizen.data.repository.sources.LocalPreferencesDataSource
import org.towny.kaizen.data.repository.sources.RemoteFirestoreDataSource
import org.towny.kaizen.domain.repository.ChallengesRepository
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.repository.UsersRepository
import org.towny.kaizen.domain.services.ChallengesService
import org.towny.kaizen.domain.services.AuthService
import org.towny.kaizen.domain.usecases.GetReloadedUserSessionUseCase
import org.towny.kaizen.ui.screens.account.AccountViewModel
import org.towny.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import org.towny.kaizen.ui.screens.add_friends.AddFriendsViewModel
import org.towny.kaizen.ui.screens.home.HomeViewModel
import org.towny.kaizen.ui.screens.login.AuthViewModel
import org.towny.kaizen.ui.screens.onboarding.OnboardingProfileViewModel

val commonModules = module {
    // ViewModels
    viewModelOf(::HomeViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::AddFriendsViewModel)
    viewModelOf(::CreateChallengeViewModel)
    viewModelOf(::OnboardingProfileViewModel)

    // Service
    singleOf(::AuthService)
    singleOf(::ChallengesService)

    // Use cases
    singleOf(::GetReloadedUserSessionUseCase)

    // Repository
    singleOf(::UsersRepositoryImpl).bind<UsersRepository>()
    singleOf(::ChallengesRepositoryImpl).bind<ChallengesRepository>()
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

    // Data sources
    singleOf(::LocalPreferencesDataSourceImpl).bind<LocalPreferencesDataSource>()
    singleOf(::RemoteFirestoreDataSourceImpl).bind<RemoteFirestoreDataSource>()
    singleOf(::FirebaseAuth)
}

expect val targetModule: Module