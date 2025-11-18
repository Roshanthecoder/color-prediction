package com.hubmasti.mastihub.presentation.ui.colorprediction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hubmasti.mastihub.data.local.UserDataStore
import com.hubmasti.mastihub.presentation.navigation.NavRoutes
import com.hubmasti.mastihub.presentation.ui.uiUtils.UiState
import kotlinx.coroutines.launch

@Composable
fun ColorPrediction(
    navController: NavController,
    viewModel: GameViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val context = LocalContext.current
    var isLoggingOut by remember { mutableStateOf(false) }
    // Load game period & countdown once
    val userDataStore = remember { UserDataStore(context) }
    //  val userDataStore = remember { UserDataStore(context = UserDataStore(context)) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.load()
        viewModel.gameLogsLoad() // Game logs load only once
    }

    val state by viewModel.uiState.collectAsState()
    val gameLogsState by viewModel.gameLogsList.collectAsState()

    val periodValue = (state as? UiState.Success)?.data?.period?.toString() ?: "___"
    val countdownValue = (state as? UiState.Success)?.let {
        String.format("%02d:%02d", it.data.minutes, it.data.seconds)
    } ?: "00:00"

    val gameLogs = (gameLogsState as? UiState.Success)?.data.orEmpty()

    // Reload countdown when it reaches 00:00
    LaunchedEffect(countdownValue) {
        if (countdownValue == "00:00") {
            kotlinx.coroutines.delay(500)
            viewModel.load()
            viewModel.gameLogsLoad()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF5F5F5))
            .padding(12.dp)
    ) {
        // Balance Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D6EFD)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(7.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.fillMaxHeight(),
                        text = "Available balance: ₹0.00",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                isLoggingOut = true
                                userDataStore.clear() // Clear data
                                kotlinx.coroutines.delay(500)
                                navController.navigate(NavRoutes.LOGIN.name) {
                                    popUpTo(0) { inclusive = true }
                                }
                                isLoggingOut = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        if (isLoggingOut) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Wait a moment...", color = Color.White)
                        } else {
                            Text("Logout", color = Color.White)
                        }
                    }

                }
                Spacer(Modifier.height(12.dp))
                Row {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(Color(0xFF28A745))
                    ) { Text("Recharge", color = Color.White) }
                    Spacer(Modifier.width(10.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) { Text("Read Rule", color = Color.Black) }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Period & Countdown
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Period", fontWeight = FontWeight.Bold, color = Color.Black)
                Text(periodValue, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Count Down", fontWeight = FontWeight.Bold, color = Color.Black)
                Text(countdownValue, color = Color.Gray)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Main Color Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ColorButton("Join Green", Color(0xFF28A745), textSize = 12.sp)
            ColorButton("Join Violet", Color(0xFF6F42C1), textSize = 12.sp)
            ColorButton("Join Red", Color(0xFFDC3545), textSize = 12.sp)
        }

        Spacer(Modifier.height(20.dp))

        // Number grid
        val numbers = (0..9).toList()
        val colors = listOf(
            Color(0xFF6F42C1), Color(0xFF28A745), Color.Red, Color(0xFF28A745), Color.Red,
            Color(0xFF6F42C1), Color.Red, Color(0xFF28A745), Color.Red, Color(0xFF28A745)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(numbers.size) { index ->
                NumberBox(numbers[index].toString(), colors[index])
            }
        }

        Spacer(Modifier.height(20.dp))

        // Record Header
        Text(
            "Parity Record",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        Spacer(Modifier.height(12.dp))

        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("Period", "Price", "Number", "Result").forEach {
                Text(
                    it,
                    modifier = Modifier.weight(1f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
        }

        // Game Logs Rows
        gameLogs.forEach { log ->
            RecordRow(
                period = log.period.toString(),
                price = log.resultPrice.toString(),
                number = log.resultNum.toString()
            )
        }
    }
}

@Composable
fun ColorButton(text: String, bg: Color, textSize: TextUnit = 16.sp) {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(bg),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, color = Color.White, fontSize = textSize)
    }
}

@Composable
fun NumberBox(num: String, bg: Color) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .size(50.dp)
            .background(bg, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(num, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RecordRow(period: String, price: String, number: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            period,
            modifier = Modifier.weight(1f),
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Text(
            price,
            modifier = Modifier.weight(1f),
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Text(
            number,
            modifier = Modifier.weight(1f),
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = if (number.toInt() % 2 == 0) Color.Red else Color(0xFF28A745)
        )
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        if (number.toInt() % 2 == 0) Color.Red else Color(0xFF28A745),
                        CircleShape
                    )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewColorPrediction() {
    ColorPrediction(navController = NavController(LocalContext.current))
}
