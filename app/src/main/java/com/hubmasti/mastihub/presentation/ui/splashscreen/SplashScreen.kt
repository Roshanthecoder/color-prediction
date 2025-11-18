package com.hubmasti.mastihub.presentation.ui.splashscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hubmasti.mastihub.data.local.UserDataStore
import com.hubmasti.mastihub.data.remote.ApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.hubmasti.mastihub.presentation.navigation.NavRoutes

@Composable
fun SplashScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val dataStore = remember { UserDataStore(context) }
    val scope = rememberCoroutineScope()

    // UI state
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7CD)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }

    LaunchedEffect(Unit) {
        ApiClient.cookieManager.loadFromDataStore(dataStore)
    }

    LaunchedEffect(Unit) {
        delay(1500) // splash delay

        val cookies = dataStore.cookies.first()
        if (cookies.isNotEmpty()) {
            val token = dataStore.token  // read from UserDataStore
            ApiClient.setToken(token.toString())
            navController.navigate(NavRoutes.COLOR_PREDICTION.name) {
                popUpTo(NavRoutes.SPLASH.name) { inclusive = true } // Splash remove ho jaye
                launchSingleTop = true
            }
        } else {
            navController.navigate(NavRoutes.LOGIN.name) {
                popUpTo(NavRoutes.SPLASH.name) { inclusive = true } // Splash remove ho jaye
                launchSingleTop = true
            }
        }
    }

}
