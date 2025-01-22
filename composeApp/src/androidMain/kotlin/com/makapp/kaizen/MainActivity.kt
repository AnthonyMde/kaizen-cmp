package com.makapp.kaizen

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.makapp.kaizen.app.App
import com.makapp.kaizen.domain.app.initializeUserSession

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        // TODO: Use this line when lightTheme is not forced anymore.
        //insetsController.isAppearanceLightStatusBars = isLightTheme()
        insetsController.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.TRANSPARENT

        setContent {
            App(userSession = initializeUserSession())
        }
    }

    @Suppress("unused")
    private fun isLightTheme(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> true // Light mode
            Configuration.UI_MODE_NIGHT_YES -> false // Dark mode
            else -> true // Default to light
        }
    }
}
