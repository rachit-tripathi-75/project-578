package com.example.bankingapp.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bankingapp.R
import com.example.bankingapp.classes.Document

class DocumentAdapter(
    private val documents: List<Document>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val document = documents[position]
        holder.bind(document)

        // Apply animation to each item
        holder.itemView.animation = AnimationUtils.loadAnimation(
            holder.itemView.context,
            R.anim.item_animation_fall_down
        )

        holder.viewPdfButton.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int = documents.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.documentTitle)
        val viewPdfButton: TextView = itemView.findViewById(R.id.viewPdfButton)

        fun bind(document: Document) {
            titleView.text = "${document.year} (${document.id})"
        }
    }
}