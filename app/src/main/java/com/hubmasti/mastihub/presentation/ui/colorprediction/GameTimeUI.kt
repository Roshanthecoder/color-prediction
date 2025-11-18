package com.hubmasti.mastihub.presentation.ui.colorprediction

import com.hubmasti.mastihub.data.model.GameObj


data class TimerUI(
    val minutes: Int,
    val seconds: Int,
    val rawObj: GameObj
)