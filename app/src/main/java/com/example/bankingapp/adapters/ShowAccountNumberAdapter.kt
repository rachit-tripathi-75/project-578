package com.example.bankingapp.adapters

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import com.example.bankingapp.models.AccountNumberModel
import okhttp3.internal.notify

class ShowAccountNumberAdapter(context: Context, private val accountNumbersList: List<AccountNumberModel>) : ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line) {
    private val suggestions = mutableListOf<AccountNumberModel>()
    override fun getCount(): Int = suggestions.size

    // getItem also needs to handle nullability for accountNumberInt
    override fun getItem(position: Int): String? = suggestions[position].accountNumberInt

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val results = FilterResults()
                if (constraint != null) {
                    val query = constraint.toString().lowercase()
                    val filteredList = accountNumbersList.filter {


                        (it.accountNumberInt?.contains(query, true) ?: false) ||
                                (it.typeOfAccount?.contains(query, true) ?: false) ||
                                (it.name?.contains(query, true) ?: false) ||
                                (it.nominee?.contains(query, true) ?: false) ||
                                (it.memId?.contains(query, true) ?: false) ||
                                (it.father?.contains(query, true) ?: false) ||
                                (it.cifNumber?.contains(query, true) ?: false)
                    }
                    suggestions.clear()
                    suggestions.addAll(filteredList)
                    results.values = suggestions
                    results.count = suggestions.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }
}