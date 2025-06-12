package com.example.bankingapp.models

// Data classes
data class FastagTransactionModel(
    val id: Int,
    val amount: Int,
    val date: String,
    val status: String,
    val toll: String
)