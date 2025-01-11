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
import org.towny.kaizen.data.repository.FriendsRepositoryImpl
import org.towny.kaizen.data.repository.UsersRepositoryImpl
import org.towny.kaizen.data.repository.sources.LocalPreferencesDataSource
import org.towny.kaizen.data.repository.sources.RemoteFirestoreDataSource
import org.towny.kaizen.domain.repository.ChallengesRepository
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.repository.FriendsRepository
import org.towny.kaizen.domain.repository.UsersRepository
import org.towny.kaizen.domain.services.ChallengesService
import org.towny.kaizen.domain.services.FriendsService
import org.towny.kaizen.domain.services.AuthenticateService
import org.towny.kaizen.domain.usecases.CreateUserUseCase
import org.towny.kaizen.domain.usecases.GetReloadedUserSessionUseCase
import org.towny.kaizen.domain.usecases.VerifyUsernameAvailableUseCase
import org.towny.kaizen.domain.usecases.VerifyUsernameFormatUseCase
import org.towny.kaizen.ui.screens.account.AccountViewModel
import org.towny.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import org.towny.kaizen.ui.screens.my_friends.MyFriendsViewModel
import org.towny.kaizen.ui.screens.home.HomeViewModel
import org.towny.kaizen.ui.screens.login.AuthViewModel
import org.towny.kaizen.ui.screens.onboarding.OnboardingProfileViewModel

val commonModules = module {
    // ViewModels
    viewModelOf(::HomeViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::MyFriendsViewModel)
    viewModelOf(::CreateChallengeViewModel)
    viewModelOf(::OnboardingProfileViewModel)

    // Service
    singleOf(::AuthenticateService)
    singleOf(::ChallengesService)
    singleOf(::FriendsService)

    // Use cases
    singleOf(::GetReloadedUserSessionUseCase)
    singleOf(::CreateUserUseCase)
    singleOf(::VerifyUsernameAvailableUseCase)
    singleOf(::VerifyUsernameFormatUseCase)

    // Repository
    singleOf(::UsersRepositoryImpl).bind<UsersRepository>()
    singleOf(::ChallengesRepositoryImpl).bind<ChallengesRepository>()
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::FriendsRepositoryImpl).bind<FriendsRepository>()

    // Data sources
    singleOf(::LocalPreferencesDataSourceImpl).bind<LocalPreferencesDataSource>()
    singleOf(::RemoteFirestoreDataSourceImpl).bind<RemoteFirestoreDataSource>()
    singleOf(::FirebaseAuth)
}

expect val targetModule: Module