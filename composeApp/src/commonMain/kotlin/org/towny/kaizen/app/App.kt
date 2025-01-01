package org.towny.kaizen.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.towny.kaizen.app.navigation.LocalNavController
import org.towny.kaizen.app.navigation.Route
import org.towny.kaizen.ui.screens.account.AccountScreenRoot
import org.towny.kaizen.ui.screens.create_challenge.CreateChallengeScreenRoot
import org.towny.kaizen.ui.screens.add_friends.AddFriendsScreenRoot
import org.towny.kaizen.ui.screens.home.HomeScreenRoot
import org.towny.kaizen.ui.screens.login.LoginScreenRoot
import org.towny.kaizen.ui.theme.AppTheme

@Composable
@Preview
fun App(username: String? = null) {
    AppTheme {
        val navController = rememberNavController()

        CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(
                navController = navController,
                startDestination = if (username == null) Route.AuthenticationGraph else Route.HomeGraph,
            ) {
                navigation<Route.AuthenticationGraph>(
                    startDestination = Route.Login
                ) {
                    composable<Route.Login> {
                        LoginScreenRoot(goToHomeScreen = {
                            navController.navigate(Route.Home) {
                                popUpTo(Route.Login) { inclusive = true }
                            }
                        })
                    }
                }

                navigation<Route.HomeGraph>(
                    startDestination = Route.Home
                ) {
                    composable<Route.Home> {
                        HomeScreenRoot(
                            goToAccount = {
                                navController.navigate(Route.Account)
                            })
                    }
                }

                navigation<Route.AccountGraph>(
                    startDestination = Route.Account
                ) {
                    composable<Route.Account>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start,
                                tween(300)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(300)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(300)
                            )
                        }
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
                            goToAddFriends = {
                                navController.navigate(Route.AddFriends)
                            },
                            goToCreateChallenge = {
                                navController.navigate(Route.CreateChallenge)
                            }
                        )
                    }

                    composable<Route.AddFriends>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start,
                                tween(300)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(300)
                            )
                        }
                    ) {
                        AddFriendsScreenRoot(
                            popToAccount = {
                                navController.navigateUp()
                            }
                        )
                    }

                    composable<Route.CreateChallenge>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start,
                                tween(300)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(300)
                            )
                        }
                    ) {
                        CreateChallengeScreenRoot(
                            navigateUp = {
                                navController.navigateUp()
                            },
                            goHome = {
                                navController.navigate(Route.Home) {
                                    popUpTo<Route.Home> { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
