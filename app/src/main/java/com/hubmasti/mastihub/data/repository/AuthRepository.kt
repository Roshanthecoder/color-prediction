package com.hubmasti.mastihub.data.repository

import com.hubmasti.mastihub.data.model.LoginResponse
import com.hubmasti.mastihub.data.model.OtpResponse
import com.hubmasti.mastihub.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AuthRepository {
    private val api = ApiClient.apiService

    suspend fun getLoginRequest(mobileNumber: String, password: String): Result<LoginResponse> =
        withContext(Dispatchers.IO) {
            try {
                val resp = api.getLoginRequest(mobileNumber = mobileNumber, password = password)
                Result.success(resp)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun sentOtpToRegister(mobileNumber: String): Result<OtpResponse> =
        withContext(Dispatchers.IO) {
            try {
                val resp = api.sentOtpToRegister(mobileNumber = mobileNumber)
                Result.success(resp)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }



    suspend fun getRegisterUser(mobileNumber: String,otpCode: String,password: String): Result<LoginResponse> =
        withContext(Dispatchers.IO) {
            try {
                val resp = api.getRegisterUser(mobileNumber = mobileNumber,otpCode = otpCode, passWord = password)
                Result.success(resp)
            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

}