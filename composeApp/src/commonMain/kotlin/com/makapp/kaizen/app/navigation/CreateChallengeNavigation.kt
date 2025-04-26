package com.makapp.kaizen.app.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.makapp.kaizen.ui.screens.create_challenge.CreateChallengeViewModel
import com.makapp.kaizen.ui.screens.create_challenge.commitment.ChallengeCommitmentNavArgs
import com.makapp.kaizen.ui.screens.create_challenge.commitment.CreateChallengeCommitmentScreenRoot
import com.makapp.kaizen.ui.screens.create_challenge.expectations.ChallengeExpectationsNavArgs
import com.makapp.kaizen.ui.screens.create_challenge.expectations.CreateChallengeExpectationsScreenRoot
import com.makapp.kaizen.ui.screens.create_challenge.infos.ChallengeInfosNavArgs
import com.makapp.kaizen.ui.screens.create_challenge.infos.CreateChallengeInfosScreenRoot

fun NavGraphBuilder.CreateChallengeNavigation(
    navController: NavHostController,
) {
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
}