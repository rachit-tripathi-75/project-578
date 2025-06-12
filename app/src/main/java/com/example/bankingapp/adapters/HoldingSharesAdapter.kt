package com.example.bankingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bankingapp.R
import com.example.bankingapp.databinding.ItemShareHoldingBinding
import com.example.bankingapp.models.HoldingShares
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class HoldingSharesAdapter :
    ListAdapter<HoldingShares, HoldingSharesAdapter.HoldingViewHolder>(HoldingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding =
            ItemShareHoldingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HoldingViewHolder(private val binding: ItemShareHoldingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(holding: HoldingShares) {
            val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            formatter.currency = Currency.getInstance("INR")

            val gain = holding.currentValue - holding.investedValue
            val gainPercent = (gain / holding.investedValue) * 100

            binding.apply {
                tvSymbol.text = holding.symbol
                tvCompanyName.text = holding.companyName
                tvQuantity.text = "Qty: ${holding.quantity}"
                tvCurrentValue.text = formatter.format(holding.currentValue)
                tvCurrentPrice.text = formatter.format(holding.currentPrice)
                tvLogo.text = holding.logo

                if (gain >= 0) {
                    tvChangePercent.text = "+${String.format("%.2f", gainPercent)}%"
                    tvChangePercent.setTextColor(itemView.context.getColor(R.color.green_600))
                    ivChangeIcon.setImageResource(R.drawable.ic_arrow_up_right)
                } else {
                    tvChangePercent.text = "${String.format("%.2f", gainPercent)}%"
                    tvChangePercent.setTextColor(itemView.context.getColor(R.color.red_600))
                    ivChangeIcon.setImageResource(R.drawable.ic_arrow_down_right)
                }
            }
        }
    }

    class HoldingDiffCallback : DiffUtil.ItemCallback<HoldingShares>() {
        override fun areItemsTheSame(oldItem: HoldingShares, newItem: HoldingShares): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: HoldingShares, newItem: HoldingShares): Boolean {
            return oldItem == newItem
        }
    }

}