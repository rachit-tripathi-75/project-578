package com.example.bankingapp.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bankingapp.R
import com.example.bankingapp.classes.UpiOption


class UpiOptionAdapter(
    private val options: List<UpiOption>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<UpiOptionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_upi_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option)

        // Apply animation to each item
        holder.itemView.animation = AnimationUtils.loadAnimation(
            holder.itemView.context,
            R.anim.item_animation_fall_down
        )

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = options.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconView: ImageView = itemView.findViewById(R.id.optionIcon)
        private val nameView: TextView = itemView.findViewById(R.id.optionName)

        fun bind(option: UpiOption) {
            iconView.setImageResource(option.iconResId)
            nameView.text = option.name
        }
    }
}