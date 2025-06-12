package com.example.bankingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bankingapp.R
import com.example.bankingapp.databinding.ItemFastagTransactionBinding
import com.example.bankingapp.models.FastagTransactionModel

class FastTagTransactionAdapter(private val transactions: List<FastagTransactionModel>) : RecyclerView.Adapter<FastTagTransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemFastagTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FastTagTransactionAdapter.TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])

    }



    override fun getItemCount() = transactions.size

    class TransactionViewHolder(private val binding: ItemFastagTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: FastagTransactionModel) {
            binding.apply {
                tvTollName.text = transaction.toll
                tvTransactionDate.text = transaction.date
                tvAmount.text = "₹${transaction.amount}"
                tvStatus.text = transaction.status

                // Set status color
                when (transaction.status.lowercase()) {
                    "success" -> {
                        binding.tvStatus.setTextColor(ContextCompat.getColor(root.context, R.color.success_color))
                        tvStatus.background = ContextCompat.getDrawable(root.context, R.drawable.status_success_bg)
                    }
                    "failed" -> {
                        tvStatus.setTextColor(ContextCompat.getColor(root.context, R.color.error_color))
                        tvStatus.background = ContextCompat.getDrawable(root.context, R.drawable.status_failed_bg)
                    }
                    "pending" -> {
                        tvStatus.setTextColor(ContextCompat.getColor(root.context, R.color.warning_color))
                        tvStatus.background = ContextCompat.getDrawable(root.context, R.drawable.status_pending_bg)
                    }
                }
            }
        }
    }
}