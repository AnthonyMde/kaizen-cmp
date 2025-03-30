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
import com.makapp.kaizen.domain.models.user.UserSession
import com.makapp.kaizen.ui.screens.account.AccountScreenRoot
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsNavArgs
import com.makapp.kaizen.ui.screens.challenge_details.ChallengeDetailsScreenRoot
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import com.makapp.kaizen.ui.screens.create_challenge.commitment.ChallengeCommitmentNavArgs
import com.makapp.kaizen.ui.screens.create_challenge.commitment.CreateChallengeCommitmentScreenRoot
import com.makapp.kaizen.ui.screens.create_challenge.expectations.ChallengeExpectationsNavArgs
import com.makapp.kaizen.ui.screens.create_challenge.expectations.CreateChallengeExpectationsScreenRoot
import com.makapp.kaizen.ui.screens.create_challenge.infos.ChallengeInfosNavArgs
import com.makapp.kaizen.ui.screens.create_challenge.infos.CreateChallengeInfosScreenRoot
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
                    startDestination = Route.CreateChallengeInfosStep(
                        editing = false
                    ),
                ) {
                    // CREATE CHALLENGE INFOS STEP
                    composable<Route.CreateChallengeInfosStep>(
                        enterTransition = { defaultEnterTransition() },
                        popEnterTransition = { defaultPopEnterTransition() },
                        popExitTransition = { defaultPopExitTransition() }
                    ) { backStackEntry ->
                        val editing = backStackEntry.arguments?.getBoolean("editing") ?: false
                        val challengeId = backStackEntry.arguments?.getString("challengeId")
                        val title = backStackEntry.arguments?.getString("title")
                        val lives = backStackEntry.arguments?.getInt("lives")
                        val viewModel =
                            backStackEntry.sharedViewModel<CreateChallengeViewModel>(navController)
                        CreateChallengeInfosScreenRoot(
                            viewModel = viewModel,
                            navArgs = ChallengeInfosNavArgs(
                                editing,
                                title,
                                lives,
                                challengeId
                            ),
                            navigateUp = {
                                navController.navigateUp()
                            },
                            goToExpectationsStep = {
                                navController.navigate(
                                    Route.CreateChallengeExpectationsStep(
                                        editing = false,
                                    )
                                )
                            }
                        )
                    }

                    // CREATE CHALLENGE EXPECTATIONS STEP
                    composable<Route.CreateChallengeExpectationsStep>(
                        enterTransition = { defaultEnterTransition() },
                        popEnterTransition = { defaultPopEnterTransition() },
                        popExitTransition = { defaultPopExitTransition() }
                    ) { backStackEntry ->
                        val editing = backStackEntry.arguments?.getBoolean("editing") ?: false
                        val challengeId = backStackEntry.arguments?.getString("challengeId")
                        val expectations = backStackEntry.arguments?.getString("expectations")
                        val viewModel =
                            backStackEntry.sharedViewModel<CreateChallengeViewModel>(navController)
                        CreateChallengeExpectationsScreenRoot(
                            viewModel = viewModel,
                            goToCommitmentStep = {
                                navController.navigate(Route.CreateChallengeCommitmentStep(editing = false))
                            },
                            navigateUp = {
                                navController.navigateUp()
                            },
                            navArgs = ChallengeExpectationsNavArgs(
                                editing = editing,
                                expectations = expectations,
                                challengeId = challengeId
                            )
                        )
                    }

                    // CREATE CHALLENGE COMMITMENT STEP
                    composable<Route.CreateChallengeCommitmentStep>(
                        enterTransition = { defaultEnterTransition() },
                        popExitTransition = { defaultPopExitTransition() }
                    ) { backStackEntry ->
                        val editing = backStackEntry.arguments?.getBoolean("editing") ?: false
                        val challengeId = backStackEntry.arguments?.getString("challengeId")
                        val commitment = backStackEntry.arguments?.getString("commitment")
                        val viewModel =
                            backStackEntry.sharedViewModel<CreateChallengeViewModel>(navController)
                        CreateChallengeCommitmentScreenRoot(
                            viewModel = viewModel,
                            goHome = {
                                navController.navigate(Route.Home) {
                                    popUpTo<Route.Home> { inclusive = true }
                                }
                            },
                            navigateUp = {
                                navController.navigateUp()
                            },
                            navArgs = ChallengeCommitmentNavArgs(
                                editing = editing,
                                commitment = commitment,
                                challengeId = challengeId
                            )
                        )
                    }
                }

                // CHALLENGE DETAILS
                composable<Route.ChallengeDetails>(
                    enterTransition = { defaultEnterTransition() },
                    popEnterTransition = { defaultPopEnterTransition() },
                    popExitTransition = { defaultPopExitTransition() }
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id") ?: return@composable
                    val readOnly =
                        backStackEntry.arguments?.getBoolean("readOnly") ?: return@composable

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
    }
}
