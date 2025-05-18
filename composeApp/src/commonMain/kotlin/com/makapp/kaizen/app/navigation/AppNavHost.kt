package com.makapp.kaizen.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.savedstate.read
import com.makapp.kaizen.domain.models.user.UserSession
import com.makapp.kaizen.ui.screens.account.AccountScreenRoot
import com.makapp.kaizen.ui.screens.archived_kaizens.ArchivedKaizensScreenRoot
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsNavArgs
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsScreenRoot
import com.makapp.kaizen.ui.screens.home.HomeScreenRoot
import com.makapp.kaizen.ui.screens.login.AuthScreenRoot
import com.makapp.kaizen.ui.screens.my_friends.MyFriendsScreenRoot
import com.makapp.kaizen.ui.screens.onboarding.OnboardingProfileScreenRoot
import com.makapp.kaizen.ui.screens.profile.ProfileScreenRoot

@Composable
fun AppNavHost(
    navController: NavHostController,
    userSession: UserSession?,
) {
    NavHost(
        navController = navController,
        startDestination = if (userSession == null) Route.Login else Route.Home,
    ) {
        // AUTHENTICATION
        composable<Route.Login> {
            AuthScreenRoot(
                goToHomeScreen = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                },
                goToOnboardingProfile = {
                    navController.navigate(Route.OnboardingProfile) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                })
        }


        // ONBOARDING
        composable<Route.OnboardingProfile>(
            enterTransition = { defaultEnterTransition() }
        ) {
            OnboardingProfileScreenRoot(
                goToHomeScreen = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.OnboardingProfile) { inclusive = true }
                    }
                },
            )
        }

        // HOME
        composable<Route.Home> {
            HomeScreenRoot(
                goToAccount = {
                    navController.navigate(Route.Account)
                },
                popToLogin = {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Home) { inclusive = true }
                    }
                },
                goToCreateChallenge = {
                    navController.navigate(Route.CreateChallengeInfosStep(
                        editing = false
                    ))
                },
                goToCreateUserAccount = {
                    navController.navigate(Route.OnboardingProfile) {
                        popUpTo<Route.Home> { inclusive = true }
                    }
                },
                goToFriendsScreen = {
                    navController.navigate(Route.MyFriends)
                },
                goToChallengeDetails = { args ->
                    navController.navigate(
                        Route.ChallengeDetails(
                            args.id,
                            args.readOnly
                        )
                    )
                }
            )
        }

        // ACCOUNT
        composable<Route.Account>(
            enterTransition = { defaultEnterTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() }
        ) {
            AccountScreenRoot(
                popToLogin = {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Account) { inclusive = true }
                    }
                },
                popToHome = {
                    navController.navigateUp()
                },
                goToMyFriends = {
                    navController.navigate(Route.MyFriends)
                },
                goToCreateChallenge = {
                    navController.navigate(Route.CreateChallengeGraph)
                },
                goToProfile = {
                    navController.navigate(Route.Profile)
                },
                goToArchivedKaizens = {
                    navController.navigate(Route.ArchivedKaizens)
                },
            )
        }

        // PROFILE
        composable<Route.Profile>(
            enterTransition = { defaultEnterTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() }
        ) {
            ProfileScreenRoot(
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }

        // ARCHIVED KAIZENS
        composable<Route.ArchivedKaizens>(
            enterTransition = { defaultEnterTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() }
        ) {
            ArchivedKaizensScreenRoot(
                onNavigateUp = {
                    navController.navigateUp()
                },
                goToChallengeDetails = { id ->
                    navController.navigate(
                        Route.ChallengeDetails(
                            id = id,
                            readOnly = false,
                        )
                    )
                },
            )
        }

        // FRIENDS
        composable<Route.MyFriends>(
            enterTransition = { defaultEnterTransition() },
            popExitTransition = { defaultPopExitTransition() }
        ) {
            MyFriendsScreenRoot(
                popToAccount = {
                    navController.navigateUp()
                }
            )
        }

        // CREATE CHALLENGE FUNNEL
        CreateChallengeNavigation(navController)

        // CHALLENGE DETAILS
        composable<Route.ChallengeDetails>(
            enterTransition = { defaultEnterTransition() },
            popEnterTransition = { defaultPopEnterTransition() },
            popExitTransition = { defaultPopExitTransition() }
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.read { getStringOrNull("id") } ?: return@composable
            val readOnly =
                backStackEntry.arguments?.read { getBooleanOrNull("readOnly") } ?: return@composable

            ChallengeDetailsScreenRoot(
                navArgs = ChallengeDetailsNavArgs(
                    id,
                    readOnly
                ),
                navigateUp = {
                    navController.popBackStack()
                },
                goToChallengeInfos = { lives, title ->
                    navController.navigate(
                        Route.CreateChallengeInfosStep(
                            editing = true,
                            title = title,
                            lives = lives,
                            challengeId = id
                        )
                    )
                },
                goToChallengeExpectations = { expectations ->
                    navController.navigate(
                        Route.CreateChallengeExpectationsStep(
                            editing = true,
                            expectations = expectations,
                            challengeId = id
                        )
                    )
                },
                goToChallengeCommitment = { commitment ->
                    navController.navigate(
                        Route.CreateChallengeCommitmentStep(
                            editing = true,
                            commitment = commitment,
                            challengeId = id
                        )
                    )
                }
            )
        }
    }
}