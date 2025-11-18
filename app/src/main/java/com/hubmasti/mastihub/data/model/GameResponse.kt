package com.hubmasti.mastihub.data.model

data class GameResponse(
    val res: Int,
    val resMsg: String,
    val obj: GameObj?
)

data class GameObj(
    val gameType: String?,
    val period: Long?,
    val gameTime: Long?,
    val pourEnd: Long?,
    val overTime: Long?,
    val startTime: Long?,
    val openTime: Long?,
    val nowTime: Long?,

    var minutes: Int = 0,
    var seconds: Int = 0
)
