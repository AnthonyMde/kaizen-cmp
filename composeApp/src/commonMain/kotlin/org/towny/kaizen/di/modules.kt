package org.towny.kaizen.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.towny.kaizen.data.local.DataStoreDataSourceImpl
import org.towny.kaizen.data.remote.firebase_auth.FirebaseAuthDataSourceImpl
import org.towny.kaizen.data.remote.firebase_functions.FirebaseFunctionsDataSourceImpl
import org.towny.kaizen.data.remote.firestore.RemoteFirestoreDataSourceImpl
import org.towny.kaizen.data.repository.ChallengesRepositoryImpl
import org.towny.kaizen.data.repository.AuthRepositoryImpl
import org.towny.kaizen.data.repository.FriendRequestsRepositoryImpl
import org.towny.kaizen.data.repository.FriendsRepositoryImpl
import org.towny.kaizen.data.repository.UsersRepositoryImpl
import org.towny.kaizen.data.repository.sources.DataStoreDataSource
import org.towny.kaizen.data.repository.sources.FirebaseAuthDataSource
import org.towny.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import org.towny.kaizen.data.repository.sources.FirestoreDataSource
import org.towny.kaizen.domain.repository.ChallengesRepository
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.repository.FriendRequestsRepository
import org.towny.kaizen.domain.repository.FriendsRepository
import org.towny.kaizen.domain.repository.UsersRepository
import org.towny.kaizen.domain.services.ChallengesService
import org.towny.kaizen.domain.services.FriendRequestsService
import org.towny.kaizen.domain.services.AuthenticateService
import org.towny.kaizen.domain.services.FriendsService
import org.towny.kaizen.domain.usecases.CreateUserUseCase
import org.towny.kaizen.domain.usecases.GetFriendPreviewUseCase
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
    singleOf(::FriendRequestsService)
    singleOf(::FriendsService)

    // Use cases
    singleOf(::GetReloadedUserSessionUseCase)
    singleOf(::CreateUserUseCase)
    singleOf(::VerifyUsernameAvailableUseCase)
    singleOf(::VerifyUsernameFormatUseCase)
    singleOf(::GetFriendPreviewUseCase)

    // Repository
    singleOf(::UsersRepositoryImpl).bind<UsersRepository>()
    singleOf(::ChallengesRepositoryImpl).bind<ChallengesRepository>()
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::FriendRequestsRepositoryImpl).bind<FriendRequestsRepository>()
    singleOf(::FriendsRepositoryImpl).bind<FriendsRepository>()

    // Data sources
    singleOf(::DataStoreDataSourceImpl).bind<DataStoreDataSource>()
    singleOf(::RemoteFirestoreDataSourceImpl).bind<FirestoreDataSource>()
    singleOf(::FirebaseAuthDataSourceImpl).bind<FirebaseAuthDataSource>()
    singleOf(::FirebaseFunctionsDataSourceImpl).bind<FirebaseFunctionsDataSource>()
}

expect val targetModule: Module