package com.example.bankingapp.models


data class HoldingShares(
    val symbol: String,
    val companyName: String,
    val quantity: Int,
    val currentPrice: Double,
    val investedValue: Double,
    val currentValue: Double,
    val logo: String
)