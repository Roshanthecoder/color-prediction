package com.hubmasti.mastihub.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hubmasti.mastihub.presentation.ui.colorprediction.ColorPrediction
import com.hubmasti.mastihub.presentation.ui.dialogsui.HlsPlayerWithFullscreen
import com.hubmasti.mastihub.presentation.ui.dialogsui.M3U8Screen
import com.hubmasti.mastihub.presentation.ui.login.LoginScreen
import com.hubmasti.mastihub.presentation.ui.signup.SignUpScreen
import com.hubmasti.mastihub.presentation.ui.splashscreen.SplashScreen


@Composable
fun SetupNavigation() {
    val navController = rememberNavController()
    SetupNavigation(navController)
}

@Composable
fun SetupNavigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = NavRoutes.SPLASH.name) {
        composable(NavRoutes.HOME.name) {
            // HomeScreen(navController)
        }
        composable(NavRoutes.LOGIN.name) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate(NavRoutes.COLOR_PREDICTION.name) {
                        popUpTo(NavRoutes.LOGIN.name) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(NavRoutes.SPLASH.name) {
            SplashScreen(navController = navController)
        }
        composable(NavRoutes.REGISTER.name) {
            SignUpScreen(navController = navController)

        }
        composable(NavRoutes.FORGOT_PASSWORD.name) {

        }
        composable(NavRoutes.OTP_SCREEN.name) {

           // OtpScreen(navController = navController)
        }
        composable(NavRoutes.OTP_ENTER.name) {

        }
        composable(NavRoutes.COLOR_PREDICTION.name) {
            ColorPrediction(navController=navController)
        }
        composable(NavRoutes.CHAT_SCREEN.name) {
          //  ChatScreen(navController=navController)
        }
        composable(NavRoutes.M3U8_INPUT.name) {
            M3U8Screen {url->
                navController.navigate(NavRoutes.HLS_PLAYER.name + "?url=$url")
            }
        }
        composable(NavRoutes.HLS_PLAYER.name + "?url={url}") { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            HlsPlayerWithFullscreen(url = url)
        }

    }
}

