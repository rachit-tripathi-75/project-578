package com.example.bankingapp.classes


class Transaction(
    val day: String,
    val month: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val isPositive: Boolean
)