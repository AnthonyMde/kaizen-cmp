package com.makapp.kaizen.di

import com.makapp.kaizen.domain.services.ChallengesService
import com.makapp.kaizen.domain.services.FriendPreviewsService
import com.makapp.kaizen.domain.services.FriendRequestsService
import com.makapp.kaizen.domain.services.FriendsService
import com.makapp.kaizen.domain.services.UsersService
import com.makapp.kaizen.domain.usecases.auth.AuthenticateUserUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val serviceModule = module {
    singleOf(::UsersService)
    singleOf(::AuthenticateUserUseCase)
    singleOf(::ChallengesService)
    singleOf(::FriendRequestsService)
    singleOf(::FriendsService)
    singleOf(::FriendPreviewsService)
}