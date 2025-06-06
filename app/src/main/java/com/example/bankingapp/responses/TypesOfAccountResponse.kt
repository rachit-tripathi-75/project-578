package com.example.bankingapp.responses

data class TypesOfAccountResponse(
    val status: String,
    val data: List<TypesOfAccount>
)

data class TypesOfAccount(
    val Id: String,
    val Name: String,
    val Abr: String
)
