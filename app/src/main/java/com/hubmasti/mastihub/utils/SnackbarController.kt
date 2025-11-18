package com.hubmasti.mastihub.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackbarController {

    private val _events = MutableSharedFlow<SnackbarEvent>()
    val events = _events.asSharedFlow()

    suspend fun showSuccess(message: String) {
        _events.emit(SnackbarEvent.Show(message, SnackbarType.SUCCESS))
    }

    suspend fun showError(message: String) {
        _events.emit(SnackbarEvent.Show(message, SnackbarType.ERROR))
    }

    suspend fun showMessage(message: String) {
        _events.emit(SnackbarEvent.Show(message, SnackbarType.DEFAULT))
    }

}
