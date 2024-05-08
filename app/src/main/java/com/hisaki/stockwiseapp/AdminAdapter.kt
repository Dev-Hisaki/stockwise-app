package com.hisaki.stockwiseapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminAdapter (
    private val itemList: MutableList<AdminData>
    ) : RecyclerView.Adapter<AdminAdapter.ItemViewHolder>() {

        class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val adminNameTextView: TextView = itemView.findViewById(R.id.adminNameTextView)
            val adminEmailTextView: TextView = itemView.findViewById(R.id.adminEmailTextView)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_item_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemData = itemList[position]
        holder.adminNameTextView.text = itemData.name
        holder.adminEmailTextView.text = itemData.email
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    }