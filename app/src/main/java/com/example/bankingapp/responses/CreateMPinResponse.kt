package com.example.bankingapp.responses

data class CreateMPinResponse(
    val Msg: String,
    val type: String,
    val status: Int,
    val data: Any?
)