package com.example.bankingapp.adapters



sealed class TransactionItem {
    data class Transaction(
        val date: String,
        val amount: String,
        val balance: String,
        val description: String
    ) : TransactionItem()

    data class OpeningBalance(val balance: String) : TransactionItem()
}