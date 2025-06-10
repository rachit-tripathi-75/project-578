package com.example.bankingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bankingapp.R
import com.example.bankingapp.responses.StatementData

class StatementAdapter(private val statementList: List<StatementData>) : RecyclerView.Adapter<StatementAdapter.StatementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatementAdapter.StatementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statement, parent, false)




        return StatementViewHolder(view);
    }

    override fun onBindViewHolder(holder: StatementAdapter.StatementViewHolder, position: Int) {
        val statement = statementList[position]
        holder.sno.text = statement.sno.toString()
        holder.date.text = statement.date
        holder.description.text = statement.description.replace("&#47;", "/") // for handling HTML entity........
        holder.debit.text = statement.debit
        holder.credit.text = statement.credit
        holder.balance.text = statement.balance
        holder.drCr.text = statement.drCr
    }

    override fun getItemCount(): Int = statementList.size

    class StatementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sno: TextView = itemView.findViewById(R.id.tv_s_no)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val description: TextView = itemView.findViewById(R.id.tv_description)
        val debit: TextView = itemView.findViewById(R.id.tv_debit)
        val credit: TextView = itemView.findViewById(R.id.tv_credit)
        val balance: TextView = itemView.findViewById(R.id.tv_balance)
        val drCr: TextView = itemView.findViewById(R.id.tv_dr_cr)
    }
}