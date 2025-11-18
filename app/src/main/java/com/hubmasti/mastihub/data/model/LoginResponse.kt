package com.hubmasti.mastihub.data.model

data class LoginResponse(
    val res: Int,
    val resMsg: String,
    val obj: UserData?
)

data class UserData(
    val balance: Double,
    val isAgent: Int,
    val isEjectNotice: Int,
    val mobile: String,
    val promotoId: Int,
    val user: String,
    val token: String
)
