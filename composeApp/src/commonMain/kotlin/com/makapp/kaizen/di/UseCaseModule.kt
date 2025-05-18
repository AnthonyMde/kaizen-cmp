package com.makapp.kaizen.di

import com.makapp.kaizen.domain.usecases.ObserveArchivedKaizensUseCase
import com.makapp.kaizen.domain.usecases.VerifyDisplayNameUseCase
import com.makapp.kaizen.domain.usecases.VerifyUsernameAvailableUseCase
import com.makapp.kaizen.domain.usecases.VerifyUsernameFormatUseCase
import com.makapp.kaizen.domain.usecases.auth.SendResetPasswordEmailUseCase
import com.makapp.kaizen.domain.usecases.friend.GetFriendPreviewUseCase
import com.makapp.kaizen.domain.usecases.user.CreateUserUseCase
import com.makapp.kaizen.domain.usecases.user.GetReloadedUserSessionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::GetReloadedUserSessionUseCase)
    factoryOf(::CreateUserUseCase)
    factoryOf(::VerifyUsernameAvailableUseCase)
    factoryOf(::VerifyUsernameFormatUseCase)
    factoryOf(::VerifyDisplayNameUseCase)
    factoryOf(::GetFriendPreviewUseCase)
    factoryOf(::SendResetPasswordEmailUseCase)
    factoryOf(::ObserveArchivedKaizensUseCase)
}