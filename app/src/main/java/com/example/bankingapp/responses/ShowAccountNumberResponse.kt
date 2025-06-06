package com.example.bankingapp.responses


data class ShowAccountNumberResponse(
    val status: String,
    val data: List<Accounts>
)

data class Accounts(
    val AccNoInt: String,
    val Typeofacc: String,
    val nominee: String,
    val MemId: String,
    val CIFNO: String,
    val name: String,
    val Father: String
)
