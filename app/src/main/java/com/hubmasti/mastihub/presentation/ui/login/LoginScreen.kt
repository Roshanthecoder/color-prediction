package com.hubmasti.mastihub.presentation.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hubmasti.mastihub.data.local.UserDataStore
import com.hubmasti.mastihub.data.model.LoginResponse
import com.hubmasti.mastihub.data.remote.ApiClient
import com.hubmasti.mastihub.data.repository.AuthRepository
import com.hubmasti.mastihub.presentation.navigation.NavRoutes
import com.hubmasti.mastihub.presentation.ui.uiUtils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewmodel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onLoginSuccess: (() -> Unit)? = null
) {

    val loginState by viewModel.loginState.collectAsState()

    val context = LocalContext.current
    val dataStore = remember { UserDataStore(context) }
    val repo = remember { AuthRepository() }

    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    // Detect login success
    LaunchedEffect(loginState) {
        if (loginState is UiState.Success<*>) {
            val data = (loginState as UiState.Success<LoginResponse>).data
            val cookies = ApiClient.cookieManager.getCookies()
            dataStore.saveCookies(cookies)
            kotlinx.coroutines.delay(200)
            ApiClient.setToken(data.obj?.token ?: "")
            onLoginSuccess?.invoke()
        }
    }


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

    val isFormValid = validatePhone(phone) == null && validatePassword(password) == null

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors))
            .padding(24.dp)
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Login", style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold, color = Color.Black
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
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
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
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = {
                    phoneError = validatePhone(phone)
                    passwordError = validatePassword(password)
                    if (phoneError == null && passwordError == null) {
                        viewModel.login("+91$phone", password)
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                when (loginState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Logging in...")
                    }

                    is UiState.Error -> Text("Login Failed")
                    else -> Text("Login")
                }
            }
            Spacer(modifier.height(16.dp))

           Text(modifier = Modifier.fillMaxWidth().clickable(
               onClick = {
                   navController.navigate(NavRoutes.REGISTER.name){
                       popUpTo(NavRoutes.LOGIN.name) { inclusive = true }
                       launchSingleTop = true
                   }

               }
           ),text = "Register ?", fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.End)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            navController = rememberNavController()
        )
    }
}
