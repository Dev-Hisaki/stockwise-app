package com.hisaki.stockwiseapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter (
    private val itemList: MutableList<UserData>
    ) : RecyclerView.Adapter<UserAdapter.ItemViewHolder>() {
    private var onItemClickListener: ((UserData) -> Unit)? = null
        class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
            val userEmailTextView: TextView = itemView.findViewById(R.id.userEmailTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item_layout, parent, false)
            return ItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val itemData = itemList[position]
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(itemData)
            }
            holder.userNameTextView.text = itemData.name
            holder.userEmailTextView.text = itemData.email
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

    fun setOnItemClickListener(listener: (UserData) -> Unit) {
        this.onItemClickListener = listener
    }
}