package com.makapp.kaizen.di

import com.makapp.kaizen.ui.screens.account.AccountViewModel
import com.makapp.kaizen.ui.screens.archived_kaizens.ArchivedKaizensViewModel
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsViewModel
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import com.makapp.kaizen.ui.screens.home.HomeViewModel
import com.makapp.kaizen.ui.screens.login.AuthViewModel
import com.makapp.kaizen.ui.screens.my_friends.MyFriendsViewModel
import com.makapp.kaizen.ui.screens.onboarding.OnboardingProfileViewModel
import com.makapp.kaizen.ui.screens.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::MyFriendsViewModel)
    viewModelOf(::CreateChallengeViewModel)
    viewModelOf(::OnboardingProfileViewModel)
    viewModelOf(::ChallengeDetailsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ArchivedKaizensViewModel)
}