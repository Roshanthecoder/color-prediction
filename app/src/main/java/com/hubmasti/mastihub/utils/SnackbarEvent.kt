package com.hubmasti.mastihub.utils

sealed class SnackbarEvent {
    data class Show(
        val message: String,
        val type: SnackbarType = SnackbarType.DEFAULT
    ) : SnackbarEvent()
}

enum class SnackbarType { SUCCESS, ERROR, DEFAULT }
