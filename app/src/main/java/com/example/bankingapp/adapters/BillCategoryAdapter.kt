package com.example.bankingapp.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bankingapp.R
import com.example.bankingapp.classes.BillCategory


class BillCategoryAdapter(
    private val categories: List<BillCategory>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<BillCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bill_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)

        // Apply animation to each item
        holder.itemView.animation = AnimationUtils.loadAnimation(
            holder.itemView.context,
            R.anim.item_animation_fall_down
        )

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = categories.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconView: ImageView = itemView.findViewById(R.id.categoryIcon)
        private val nameView: TextView = itemView.findViewById(R.id.categoryName)

        fun bind(category: BillCategory) {
            iconView.setImageResource(category.iconResId)
            nameView.text = category.name
        }
    }
}