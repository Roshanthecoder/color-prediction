package com.hubmasti.mastihub.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubmasti.mastihub.data.model.LoginResponse
import com.hubmasti.mastihub.data.model.OtpResponse
import com.hubmasti.mastihub.data.repository.AuthRepository
import com.hubmasti.mastihub.presentation.ui.uiUtils.UiState
import com.hubmasti.mastihub.utils.SnackbarController
import com.hubmasti.mastihub.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewmodel(
    private val repo: AuthRepository = AuthRepository(),
) : ViewModel() {


    var loginState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
        private set

    var otpSentState = MutableStateFlow<UiState<OtpResponse>>(UiState.Idle)
        private set



    var registerUserState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
        private set



    fun registerNewUser(mobile: String,password: String,otpCode: String) {
        viewModelScope.launch {
            registerUserState.value = UiState.Loading
            try {
                val encodePassword = Utils.md5Hash(password)
                val response = repo.getRegisterUser(mobileNumber = mobile, otpCode = otpCode, password = encodePassword)
                if (response.isSuccess) {
                    val body = response.getOrNull()

                    if (body != null && body.res == 1) {
                        registerUserState.value = UiState.Success(body)
                        SnackbarController.showSuccess("Otp Sent Sucessfully")
                    } else {
                        registerUserState.value = UiState.Error("Password Not Correct")
                        SnackbarController.showError("Otp Not Sent")
                    }
                } else {
                    val msg = response.exceptionOrNull()?.message ?: "Unknown error"
                    registerUserState.value = UiState.Error(msg)
                    SnackbarController.showError(msg)
                }

            } catch (e: Exception) {
                registerUserState.value = UiState.Error(e.message ?: "Something went wrong")
                SnackbarController.showError(e.message ?: "Something went wrong")
            }
        }
    }



    fun sentOtpRequest(mobile: String) {
        viewModelScope.launch {
            otpSentState.value = UiState.Loading
            try {
               // val encodePassword = Utils.md5Hash(password)
                val response = repo.sentOtpToRegister(mobileNumber = mobile)
                if (response.isSuccess) {
                    val body = response.getOrNull()

                    if (body != null && body.res == 1) {
                        otpSentState.value = UiState.Success(body)
                        SnackbarController.showSuccess("Otp Sent Sucessfully")
                    } else {
                        otpSentState.value = UiState.Error("Password Not Correct")
                        SnackbarController.showError("Otp Not Sent")
                    }
                } else {
                    val msg = response.exceptionOrNull()?.message ?: "Unknown error"
                    otpSentState.value = UiState.Error(msg)
                    SnackbarController.showError(msg)
                }

            } catch (e: Exception) {
                otpSentState.value = UiState.Error(e.message ?: "Something went wrong")
                SnackbarController.showError(e.message ?: "Something went wrong")
            }
        }
    }

    fun login(mobile: String, password: String) {
        viewModelScope.launch {
            loginState.value = UiState.Loading
            try {

                val encodePassword = Utils.md5Hash(password)
                val response = repo.getLoginRequest(mobile, password = encodePassword)
                if (response.isSuccess) {
                    val body = response.getOrNull()

                    if (body != null && body.res == 1) {
                        loginState.value = UiState.Success(body)
                        SnackbarController.showSuccess("Login Sucessfully")
                    } else {
                        loginState.value = UiState.Error("Password Not Correct")
                        SnackbarController.showError("Password Not Correct")
                    }
                } else {
                    val msg = response.exceptionOrNull()?.message ?: "Unknown error"
                    loginState.value = UiState.Error(msg)
                    SnackbarController.showError(msg)
                }

            } catch (e: Exception) {
                loginState.value = UiState.Error(e.message ?: "Something went wrong")
                SnackbarController.showError(e.message ?: "Something went wrong")
            }
        }
    }
}