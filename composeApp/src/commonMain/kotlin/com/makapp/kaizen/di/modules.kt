package com.makapp.kaizen.di

import com.makapp.kaizen.data.local.DataStoreDataSourceImpl
import com.makapp.kaizen.data.local.room.AppDatabase
import com.makapp.kaizen.data.local.room.friendPreviews.FriendPreviewsDao
import com.makapp.kaizen.data.local.room.friendRequests.FriendRequestsDao
import com.makapp.kaizen.data.local.room.friends.FriendsDao
import com.makapp.kaizen.data.remote.firebase_auth.FirebaseAuthDataSourceImpl
import com.makapp.kaizen.data.remote.firebase_functions.FirebaseFunctionsDataSourceImpl
import com.makapp.kaizen.data.remote.firestore.RemoteFirestoreDataSourceImpl
import com.makapp.kaizen.data.repository.AuthRepositoryImpl
import com.makapp.kaizen.data.repository.ChallengesRepositoryImpl
import com.makapp.kaizen.data.repository.FriendPreviewsRepositoryImpl
import com.makapp.kaizen.data.repository.FriendRequestsRepositoryImpl
import com.makapp.kaizen.data.repository.FriendsRepositoryImpl
import com.makapp.kaizen.data.repository.UsersRepositoryImpl
import com.makapp.kaizen.data.repository.sources.DataStoreDataSource
import com.makapp.kaizen.data.repository.sources.FirebaseAuthDataSource
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.repository.sources.FirestoreDataSource
import com.makapp.kaizen.domain.repository.AuthRepository
import com.makapp.kaizen.domain.repository.ChallengesRepository
import com.makapp.kaizen.domain.repository.FriendPreviewsRepository
import com.makapp.kaizen.domain.repository.FriendRequestsRepository
import com.makapp.kaizen.domain.repository.FriendsRepository
import com.makapp.kaizen.domain.repository.UsersRepository
import com.makapp.kaizen.domain.services.AuthenticateService
import com.makapp.kaizen.domain.services.ChallengesService
import com.makapp.kaizen.domain.services.FriendPreviewsService
import com.makapp.kaizen.domain.services.FriendRequestsService
import com.makapp.kaizen.domain.services.FriendsService
import com.makapp.kaizen.domain.services.UsersService
import com.makapp.kaizen.domain.usecases.CreateUserUseCase
import com.makapp.kaizen.domain.usecases.GetFriendPreviewUseCase
import com.makapp.kaizen.domain.usecases.GetReloadedUserSessionUseCase
import com.makapp.kaizen.domain.usecases.VerifyUsernameAvailableUseCase
import com.makapp.kaizen.domain.usecases.VerifyUsernameFormatUseCase
import com.makapp.kaizen.ui.screens.account.AccountViewModel
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import com.makapp.kaizen.ui.screens.home.HomeViewModel
import com.makapp.kaizen.ui.screens.login.AuthViewModel
import com.makapp.kaizen.ui.screens.my_friends.MyFriendsViewModel
import com.makapp.kaizen.ui.screens.onboarding.OnboardingProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModules = module {
    // ViewModels
    viewModelOf(::HomeViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::MyFriendsViewModel)
    viewModelOf(::CreateChallengeViewModel)
    viewModelOf(::OnboardingProfileViewModel)

    // Service
    singleOf(::UsersService)
    singleOf(::AuthenticateService)
    singleOf(::ChallengesService)
    singleOf(::FriendRequestsService)
    singleOf(::FriendsService)
    singleOf(::FriendPreviewsService)

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
    singleOf(::FriendPreviewsRepositoryImpl).bind<FriendPreviewsRepository>()

    // Data sources
    singleOf(::DataStoreDataSourceImpl).bind<DataStoreDataSource>()
    singleOf(::RemoteFirestoreDataSourceImpl).bind<FirestoreDataSource>()
    singleOf(::FirebaseAuthDataSourceImpl).bind<FirebaseAuthDataSource>()
    singleOf(::FirebaseFunctionsDataSourceImpl).bind<FirebaseFunctionsDataSource>()

    // Dao
    single<FriendRequestsDao> { getFriendRequestsDao(get()) }
    single<FriendPreviewsDao> { getFriendPreviewsDao(get()) }
    single<FriendsDao> { getFriendsDao(get()) }

    // Coroutines
    single<CoroutineScope> {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}

private fun getFriendRequestsDao(db: AppDatabase): FriendRequestsDao = db.getFriendRequestsDao()
private fun getFriendPreviewsDao(db: AppDatabase): FriendPreviewsDao = db.getFriendPreviewsDao()
private fun getFriendsDao(db: AppDatabase): FriendsDao = db.getFriendsDao()

expect val targetModule: Module