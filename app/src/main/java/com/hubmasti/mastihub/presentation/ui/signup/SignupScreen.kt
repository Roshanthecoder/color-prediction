package com.hubmasti.mastihub.presentation.ui.signup

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hubmasti.mastihub.data.model.LoginResponse
import com.hubmasti.mastihub.data.model.OtpResponse
import com.hubmasti.mastihub.presentation.navigation.NavRoutes
import com.hubmasti.mastihub.presentation.ui.login.AuthViewmodel
import com.hubmasti.mastihub.presentation.ui.uiUtils.UiState
import com.hubmasti.mastihub.utils.SnackbarController
import com.hubmasti.mastihub.utils.SnackbarEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewmodel = viewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var otpSending by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val otpSentState by viewModel.otpSentState.collectAsState()
    val registerUserState by viewModel.registerUserState.collectAsState()

    fun validatePhone(p: String): String? {
        val trimmed = p.trim()
        if (trimmed.isEmpty()) return "Number required"
        if (!trimmed.all { it.isDigit() }) return "Sirf digits likhein"
        if (trimmed.length != 10) return "10 digit number daalein"
        return null
    }

    fun validatePassword(p: String): String? {
        if (p.isEmpty()) return "Password required"
        if (p.length < 6) return "Kam se kam 6 characters"
        return null
    }

    fun validateOtp(o: String): String? {
        val trimmed = o.trim()
        if (trimmed.isEmpty()) return "OTP required"
        if (!trimmed.all { it.isDigit() }) return "Enter Only Numbers"
        if (trimmed.length != 6) return "Enter 6 digit OTP"
        return null
    }

    val isFormValid = validatePhone(phone) == null &&
            validatePassword(password) == null &&
            validateOtp(otp) == null

// Cooldown effect for OTP
    LaunchedEffect(otpSending) {
        if (otpSending) {
            delay(30_000)
            otpSending = false
        }
    }

// Observe OTP sent state
    LaunchedEffect(otpSentState) {
        when (otpSentState) {
            is UiState.Success<*> -> {
                val data = (otpSentState as UiState.Success<OtpResponse>).data
                Log.e("roshan", "otp data: $data", )
                scope.launch {
                    SnackbarController.showSuccess("OTP sent successfully!")
                }
            }

            is UiState.Error -> {
                scope.launch {
                    SnackbarController.showError("OTP send failed: ${(otpSentState as UiState.Error).message}")
                }
            }

            else -> {}
        }
    }

// Observe registration state
    LaunchedEffect(registerUserState) {
        when (registerUserState) {
            is UiState.Success<*> -> {
                val data = (registerUserState as UiState.Success<LoginResponse>).data
                scope.launch {
                    SnackbarController.showSuccess("Registration successful!")
                }
            }

            is UiState.Error -> {
                scope.launch {
                    SnackbarController.showError("Registration failed: ${(registerUserState as UiState.Error).message}")
                }
            }

            else -> {}
        }
    }

// Collect Snackbar events
    LaunchedEffect(Unit) {
        SnackbarController.events.collect { event ->
            when (event) {
                is SnackbarEvent.Show -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    val gradientColors = listOf(
        Color(0xFFFFF1F3),
        Color(0xFFFFE8D9),
        Color(0xFFFFF7CD),
        Color(0xFFF6FEE5),
        Color(0xFFE5FFF4),
        Color(0xFFE0FBFF),
        Color(0xFFE6F0FF),
        Color(0xFFF2E8FF)
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradientColors))
                .padding(24.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Register",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        if (it.length <= 10) {
                            phone = it
                            phoneError = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Mobile number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "phone") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    ),
                    isError = phoneError != null,
                    supportingText = {
                        phoneError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = otp,
                    onValueChange = { if (it.length <= 6) otp = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Enter OTP") },
                    trailingIcon = {
                        Box(
                            modifier = Modifier
                                .clickable(
                                    enabled = !otpSending,
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    if (phone.isNotEmpty()) {
                                        if (!otpSending) {
                                            otpSending = true
                                            viewModel.sentOtpRequest(mobile = "+91$phone")
                                        }
                                    }
                                }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send OTP",
                                tint = if (otpSending) Color.Gray else Color.Blue
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "lock") },
                    trailingIcon = {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                        )
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    isError = passwordError != null,
                    supportingText = {
                        passwordError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                       // phoneError = validatePhone(phone)
                        passwordError = validatePassword(password)
                        val otpError = validateOtp(otp)
                        if (phoneError == null && passwordError == null && otpError == null) {
                            viewModel.registerNewUser(
                                "+91$phone",
                                password = password,
                                otpCode = otp
                            )
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        "Register",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(NavRoutes.LOGIN.name) {
                                popUpTo(NavRoutes.REGISTER.name) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                    text = "Login ?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        SignUpScreen(
            navController = rememberNavController()
        )
    }
}
