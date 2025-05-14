package com.example.bankingapp.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bankingapp.databinding.ItemOpeningBalanceBinding
import com.example.bankingapp.databinding.ItemTransactionPassbookBinding

class TransactionPassbookAdapter(private val transactions: List<TransactionItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_OPENING_BALANCE = 0
        private const val VIEW_TYPE_TRANSACTION = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_OPENING_BALANCE -> {
                val binding = ItemOpeningBalanceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                OpeningBalanceViewHolder(binding)
            }
            VIEW_TYPE_TRANSACTION -> {
                val binding = ItemTransactionPassbookBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TransactionViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = transactions[position]) {
            is TransactionItem.OpeningBalance -> {
                (holder as OpeningBalanceViewHolder).bind(item)
            }
            is TransactionItem.Transaction -> {
                (holder as TransactionViewHolder).bind(item)
            }
        }
    }

    override fun getItemCount(): Int = transactions.size

    override fun getItemViewType(position: Int): Int {
        return when (transactions[position]) {
            is TransactionItem.OpeningBalance -> VIEW_TYPE_OPENING_BALANCE
            is TransactionItem.Transaction -> VIEW_TYPE_TRANSACTION
        }
    }

    inner class OpeningBalanceViewHolder(private val binding: ItemOpeningBalanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionItem.OpeningBalance) {
            binding.openingBalanceTextView.text = item.balance
        }
    }

    inner class TransactionViewHolder(private val binding: ItemTransactionPassbookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionItem.Transaction) {
            binding.dateTextView.text = item.date
            binding.amountTextView.text = item.amount
            binding.balanceTextView.text = item.balance
            binding.descriptionTextView.text = item.description

            // Set color based on Dr/Cr
            if (item.amount.contains("Dr")) {
                binding.amountTextView.setTextColor(Color.parseColor("#E53935")) // Red for debit
            } else {
                binding.amountTextView.setTextColor(Color.parseColor("#43A047")) // Green for credit
            }
        }
    }
}