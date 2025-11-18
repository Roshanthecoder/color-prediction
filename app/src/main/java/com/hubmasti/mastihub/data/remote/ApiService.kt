package com.hubmasti.mastihub.data.remote

import com.hubmasti.mastihub.data.model.GameLogResponse
import com.hubmasti.mastihub.data.model.GameResponse
import com.hubmasti.mastihub.data.model.LoginResponse
import com.hubmasti.mastihub.data.model.OtpResponse
import com.hubmasti.mastihub.domain.model.Constant
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {


    @POST("${Constant.LOTTERY_BACKEND}glserver/lottery/getPeriod")
    suspend fun getPeriod(
        @Query("gameType") gameType: Int = 1
    ): GameResponse

    @POST("${Constant.LOTTERY_BACKEND}glserver/lottery/findGameLogByPage")
    suspend fun getGameLog(
        @Query("gameType") gameType: Int = 1,
        @Query("pageNum") pageNum: Int = 1,
        @Query("pageSize") pageSize: Int = 10
    ): GameLogResponse

    @POST("${Constant.LOTTERY_BACKEND}glserver/user/login")
    suspend fun getLoginRequest(
        @Query("mobile") mobileNumber: String = "",
        @Query("password") password: String = "",
    ): LoginResponse


    @POST("${Constant.LOTTERY_BACKEND}glserver/smsserver/getCode")
    suspend fun sentOtpToRegister(
        @Query("mobile") mobileNumber: String = "",
    ): OtpResponse


    @POST("${Constant.LOTTERY_BACKEND}glserver/user/regist")
    suspend fun getRegisterUser(
        @Query("mobile") mobileNumber: String = "",
        @Query("code") otpCode: String = "",
        @Query("password") passWord: String = "",
        @Query("recommendation") referCode: String = "5939771",
    ): LoginResponse


}