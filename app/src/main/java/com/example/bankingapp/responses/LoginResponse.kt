package com.example.bankingapp.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("Msg")
    val msg: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("status")
    val status: Int,

    @SerializedName("data")
    val data: UserData
)

data class UserData(
    @SerializedName("MemId")
    val memId: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("CIFNO")
    val cifNo: String,

    @SerializedName("Mobile")
    val mobile: String,

    @SerializedName("Compnyid")
    val companyId: Int,

    @SerializedName("token")
    val token: String
)