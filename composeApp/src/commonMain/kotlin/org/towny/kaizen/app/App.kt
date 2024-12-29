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
import org.towny.kaizen.ui.screens.account.AccountScreen
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
                    composable<Route.Home>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start,
                                tween(300)
                            )
                        },
                    ) {
                        HomeScreenRoot(popToLogin = {
                            navController.navigate(Route.Login) {
                                popUpTo(Route.Home) { inclusive = true }
                            }
                        })
                    }
                }

                navigation<Route.AccountGraph>(
                    startDestination = Route.Account
                ) {
                    composable<Route.Account> {
                        AccountScreen()
                    }
                }

            }
        }
    }

}