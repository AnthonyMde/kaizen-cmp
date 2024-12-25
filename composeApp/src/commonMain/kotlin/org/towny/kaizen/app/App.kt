package org.towny.kaizen.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.towny.kaizen.app.navigation.LocalNavController
import org.towny.kaizen.app.navigation.Route
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.UsersRepository
import org.towny.kaizen.ui.screens.home.HomeScreenRoot
import org.towny.kaizen.ui.screens.login.LoginScreenRoot
import org.towny.kaizen.ui.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        val navController = rememberNavController()
        // TODO: extract this logic to an AppController
        val userRepository = koinInject<UsersRepository>()
        val username = userRepository.getSavedUsername().map { result ->
            when (result) {
                is Resource.Error -> null
                is Resource.Loading -> null
                is Resource.Success -> result.data
            }
        }.collectAsState(null)

        CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(
                navController = navController,
                startDestination = if (username.value == null) Route.AuthenticationGraph else Route.HomeGraph,
            ) {
                navigation<Route.AuthenticationGraph>(
                    startDestination = Route.Login
                ) {
                    composable<Route.Login>{
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.surface)
                                .systemBarsPadding()
                        ) {
                            LoginScreenRoot(goToHomeScreen = {
                                navController.navigate(Route.Home) {
                                    popUpTo(Route.Login) { inclusive = true }
                                }
                            })
                        }
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
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.surface)
                                .systemBarsPadding()
                        ) {
                            HomeScreenRoot()
                        }
                    }
                }
            }
        }
    }
}