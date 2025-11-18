package com.hubmasti.mastihub.presentation.ui.uiUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun AppSnackbarHost(
    hostState: SnackbarHostState
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = { data ->
            val bgColor = when (data.visuals.actionLabel) {
                "ERROR" -> Color(0xFFE53935)   // red
                "SUCCESS" -> Color(0xFF4CAF50) // green
                else -> Color(0xFF323232)      // default
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(bgColor, RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = data.visuals.message,
                    color = Color.White
                )
            }
        }
    )
}
