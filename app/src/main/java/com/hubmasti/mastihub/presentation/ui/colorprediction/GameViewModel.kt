package com.hubmasti.mastihub.presentation.ui.colorprediction

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hubmasti.mastihub.data.model.GameLogItem
import com.hubmasti.mastihub.data.model.GameObj
import com.hubmasti.mastihub.data.repository.GameRepository
import com.hubmasti.mastihub.presentation.ui.uiUtils.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class GameViewModel(
    private val repo: GameRepository = GameRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GameObj>>(UiState.Idle)
    val uiState: StateFlow<UiState<GameObj>> = _uiState

    private val _gameLogsList = MutableStateFlow<UiState<List<GameLogItem>>>(UiState.Idle)
    val gameLogsList: StateFlow<UiState<List<GameLogItem>>> = _gameLogsList
    private var timerJob: Job? = null

    fun gameLogsLoad() {
        viewModelScope.launch {
            _gameLogsList.value = UiState.Loading

            val res = repo.getGamelogs(1, 1, 10)

            if (res.isSuccess) {
                val body = res.getOrNull()
                val obj = body?.obj?.results

                if (obj != null && obj.isNotEmpty()) {
                    _gameLogsList.value = UiState.Success(obj)
                } else {
                    _gameLogsList.value = UiState.Error("Empty response")
                }
            } else {
                val msg = res.exceptionOrNull()?.message ?: "Unknown error"
                _gameLogsList.value = UiState.Error(msg)
            }
        }
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val res = repo.getPeriod(1)

            if (res.isSuccess) {
                val body = res.getOrNull()
                val obj = body?.obj

                if (obj != null) {
                    _uiState.value = UiState.Success(obj)
                    startCountdown(obj)
                } else {
                    _uiState.value = UiState.Error("Empty response")
                }
            } else {
                val msg = res.exceptionOrNull()?.message ?: "Unknown error"
                _uiState.value = UiState.Error(msg)
            }
        }
    }

    private fun startCountdown(obj: GameObj) {
        timerJob?.cancel()

        val startTime = obj.startTime ?: 0L
        val nowTime = obj.nowTime ?: 0L
        val gameTime = obj.gameTime ?: 0L

        var remaining = (gameTime - (nowTime - startTime)).coerceAtLeast(0L)

        timerJob = viewModelScope.launch {
            while (remaining >= 0) {

                val min = ((remaining / 1000) / 60).toInt()
                val sec = ((remaining / 1000) % 60).toInt()

                // Create a new copy of GameObj for StateFlow
                val updatedObj = obj.copy(
                    minutes = min,
                    seconds = sec
                )

                _uiState.value = UiState.Success(updatedObj)

                delay(1000)
                remaining -= 1000
            }
        }
    }

    fun millisToIst(ms: Long?): String {
        if (ms == null || ms <= 0L) return "-"
        val fmt = SimpleDateFormat("dd MMM yyyy, hh:mm:ss a", Locale.getDefault())
        fmt.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return fmt.format(Date(ms))
    }

    fun remainingMillis(nowMs: Long, targetMs: Long): Long {
        return (targetMs - nowMs).coerceAtLeast(0L)
    }
}
