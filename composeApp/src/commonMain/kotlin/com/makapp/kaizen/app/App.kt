package com.makapp.kaizen.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.makapp.kaizen.app.navigation.LocalNavController
import com.makapp.kaizen.app.navigation.Route
import com.makapp.kaizen.app.navigation.defaultEnterTransition
import com.makapp.kaizen.app.navigation.defaultPopEnterTransition
import com.makapp.kaizen.app.navigation.defaultPopExitTransition
import com.makapp.kaizen.app.navigation.sharedViewModel
import com.makapp.kaizen.domain.models.UserSession
import com.makapp.kaizen.ui.screens.account.AccountScreenRoot
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsNavArgs
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsScreenRoot
import com.makapp.kaizen.ui.screens.create_challenge.infos.CreateChallengeInfosScreenRoot
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import com.makapp.kaizen.ui.screens.create_challenge.commitment.CreateChallengeCommitmentScreenRoot
import com.makapp.kaizen.ui.screens.home.HomeScreenRoot
import com.makapp.kaizen.ui.screens.login.AuthScreenRoot
import com.makapp.kaizen.ui.screens.my_friends.MyFriendsScreenRoot
import com.makapp.kaizen.ui.screens.onboarding.OnboardingProfileScreenRoot
import com.makapp.kaizen.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(userSession: UserSession? = null) {
    AppTheme {
        val navController = rememberNavController()

        CompositionLocalProvider(LocalNavController provides navController) {
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
                            navController.navigate(Route.CreateChallenge)
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
                                    args.title,
                                    args.isDone
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
                        }
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
                navigation<Route.CreateChallengeGraph>(
                    startDestination = Route.CreateChallenge,
                ) {
                    // CREATE CHALLENGE
                    composable<Route.CreateChallenge>(
                        enterTransition = { defaultEnterTransition() },
                        popEnterTransition = { defaultPopEnterTransition() },
                        popExitTransition = { defaultPopExitTransition() }
                    ) { backStackEntry ->
                        val viewModel = backStackEntry.sharedViewModel<CreateChallengeViewModel>(navController)
                        CreateChallengeInfosScreenRoot(
                            viewModel = viewModel,
                            navigateUp = {
                                navController.navigateUp()
                            },
                            goHome = {
                                navController.navigate(Route.Home) {
                                    popUpTo<Route.Home> { inclusive = true }
                                }
                            },
                            goToCommitmentStep = {
                                navController.navigate(Route.CreateChallengeCommitmentStep)
                            }
                        )
                    }

                    // CREATE CHALLENGE COMMITMENT STEP
                    composable<Route.CreateChallengeCommitmentStep>(
                        enterTransition = { defaultEnterTransition() },
                        popExitTransition = { defaultPopExitTransition() }
                    ) { backStackEntry ->
                        val viewModel = backStackEntry.sharedViewModel<CreateChallengeViewModel>(navController)
                        CreateChallengeCommitmentScreenRoot(
                            viewModel = viewModel,
                            navigateUp = {
                                navController.navigateUp()
                            }
                        )
                    }
                }

                // CHALLENGE DETAILS
                composable<Route.ChallengeDetails>(
                    enterTransition = { defaultEnterTransition() },
                    popExitTransition = { defaultPopExitTransition() }
                ) { backStackEntry ->
                    val title =
                        backStackEntry.arguments?.getString("title") ?: return@composable
                    val id = backStackEntry.arguments?.getString("id") ?: return@composable
                    val isDone =
                        backStackEntry.arguments?.getBoolean("isDone") ?: return@composable

                    ChallengeDetailsScreenRoot(
                        navigateUp = {
                            navController.popBackStack()
                        },
                        navArgs = ChallengeDetailsNavArgs(
                            id,
                            title,
                            isDone
                        )
                    )
                }
            }
        }
    }
}
