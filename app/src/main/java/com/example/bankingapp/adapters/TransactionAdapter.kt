package com.example.bankingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.bankingapp.R
import com.example.bankingapp.classes.Transaction
import java.lang.String


class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    private var lastPosition = -1

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.dayText.setText(transaction.day)
        holder.monthText.setText(transaction.month)
        holder.transactionTitle.setText(transaction.title)
        holder.transactionSubtitle.setText(transaction.subtitle)

        val amountText = if (transaction.isPositive) String.format(
            "%.2f",
            transaction.amount
        ) else String.format("-%.2f", transaction.amount)

        holder.amountText.text = amountText
        holder.amountText.setTextColor(
            if (transaction.isPositive) holder.itemView.context.resources.getColor(
                R.color.positive_amount
            ) else holder.itemView.context.resources.getColor(R.color.negative_amount)
        )


        // Apply animation to each item
        if (position > lastPosition) {
            holder.itemView.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(
                    holder.itemView.context, R.anim.slide_in_right
                )
            )
            lastPosition = position
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    class TransactionViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dayText: TextView = itemView.findViewById<TextView>(R.id.dayText)
        var monthText: TextView = itemView.findViewById<TextView>(R.id.monthText)
        var transactionTitle: TextView = itemView.findViewById<TextView>(R.id.transactionTitle)
        var transactionSubtitle: TextView =
            itemView.findViewById<TextView>(R.id.transactionSubtitle)
        var amountText: TextView = itemView.findViewById<TextView>(R.id.amountText)
    }
}