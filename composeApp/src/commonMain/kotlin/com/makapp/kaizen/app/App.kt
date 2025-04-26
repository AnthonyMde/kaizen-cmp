package com.makapp.kaizen.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.makapp.kaizen.app.navigation.AppNavHost
import com.makapp.kaizen.domain.models.user.UserSession
import com.makapp.kaizen.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(userSession: UserSession? = null) {
    AppTheme {
        val navController = rememberNavController()

        AppNavHost(
            userSession = userSession,
            navController = navController,
        )
    }
}
