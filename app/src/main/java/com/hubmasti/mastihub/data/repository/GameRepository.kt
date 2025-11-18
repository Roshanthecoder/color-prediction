package com.hubmasti.mastihub.data.repository

import com.hubmasti.mastihub.data.model.GameLogResponse
import com.hubmasti.mastihub.data.model.GameResponse
import com.hubmasti.mastihub.data.remote.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class GameRepository {
    private val api = ApiClient.apiService

    suspend fun getPeriod(gameType: Int = 1): Result<GameResponse> = withContext(Dispatchers.IO) {
        try {
            val resp = api.getPeriod(gameType)
            Result.success(resp)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGamelogs(
        gameType: Int = 1,
        pageNum: Int = 1,
        pageSize: Int = 10
    ): Result<GameLogResponse> = withContext(Dispatchers.IO) {
        try {
            val resp = api.getGameLog(gameType = gameType, pageNum = pageNum, pageSize = pageSize)
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
