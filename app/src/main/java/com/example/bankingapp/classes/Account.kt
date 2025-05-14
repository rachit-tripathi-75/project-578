package com.example.bankingapp.classes


data class Account(
    val accountNumber: String,
    val accountType: String,
    var balance: Double,
    val holderName: String
)
