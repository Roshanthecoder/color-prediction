package com.hubmasti.mastihub.presentation.ui.uiUtils

import com.hubmasti.mastihub.data.model.GameObj

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
