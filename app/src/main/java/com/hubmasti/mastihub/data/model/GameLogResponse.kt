package com.hubmasti.mastihub.data.model



import com.google.gson.annotations.SerializedName

data class GameLogResponse(
    @SerializedName("res") val res: Int,
    @SerializedName("resMsg") val resMsg: String,
    @SerializedName("obj") val obj: GameLogObj?
)

data class GameLogObj(
    @SerializedName("pageNum") val pageNum: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("totalRecord") val totalRecord: Int,
    @SerializedName("totalPage") val totalPage: Int,
    @SerializedName("results") val results: List<GameLogItem>,
    @SerializedName("isUseDefaultTotalRecordCount") val isUseDefaultTotalRecordCount: Boolean
)

data class GameLogItem(
    @SerializedName("logId") val logId: Long,
    @SerializedName("period") val period: Long,
    @SerializedName("gameID") val gameID: String,
    @SerializedName("gameType") val gameType: String,
    @SerializedName("resultPrice") val resultPrice: Int,
    @SerializedName("resultNum") val resultNum: Int,
    @SerializedName("source") val source: Int,
    @SerializedName("result") val result: String,
    @SerializedName("createTime") val createTime: String,
    @SerializedName("logTime") val logTime: String,
    @SerializedName("totalPour") val totalPour: Int?,    // nullable
    @SerializedName("pourNumber") val pourNumber: Int?   // nullable
)
