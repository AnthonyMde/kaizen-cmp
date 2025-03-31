package com.makapp.kaizen.di

import com.makapp.kaizen.data.repository.AuthRepositoryImpl
import com.makapp.kaizen.data.repository.ChallengesRepositoryImpl
import com.makapp.kaizen.data.repository.FriendPreviewsRepositoryImpl
import com.makapp.kaizen.data.repository.FriendRequestsRepositoryImpl
import com.makapp.kaizen.data.repository.FriendsRepositoryImpl
import com.makapp.kaizen.data.repository.UsersRepositoryImpl
import com.makapp.kaizen.domain.repository.AuthRepository
import com.makapp.kaizen.domain.repository.ChallengesRepository
import com.makapp.kaizen.domain.repository.FriendPreviewsRepository
import com.makapp.kaizen.domain.repository.FriendRequestsRepository
import com.makapp.kaizen.domain.repository.FriendsRepository
import com.makapp.kaizen.domain.repository.UsersRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::UsersRepositoryImpl).bind<UsersRepository>()
    singleOf(::ChallengesRepositoryImpl).bind<ChallengesRepository>()
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::FriendRequestsRepositoryImpl).bind<FriendRequestsRepository>()
    singleOf(::FriendsRepositoryImpl).bind<FriendsRepository>()
    singleOf(::FriendPreviewsRepositoryImpl).bind<FriendPreviewsRepository>()
}