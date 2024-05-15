package com.hisaki.stockwiseapp

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AdminAdapter(
    private val itemList: MutableList<AdminData>
) : RecyclerView.Adapter<AdminAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adminNameTextView: TextView = itemView.findViewById(R.id.adminNameTextView)
        val adminEmailTextView: TextView = itemView.findViewById(R.id.adminEmailTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.admin_item_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemData = itemList[position]
        holder.adminNameTextView.text = itemData.name
        holder.adminEmailTextView.text = itemData.email

        holder.itemView.setOnClickListener {
            detailUserDialog(itemData.name, itemData.email, holder.itemView.context)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun detailUserDialog(userName: String, userEmail: String, context: Context) {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.popup_detail_kelola_akun_user, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.show()

        dialogView.findViewById<TextView>(R.id.userNameTextView)?.text = "Admin $userName"

        val btnDeleteUser = dialogView.findViewById<TextView>(R.id.deleteUserButton)
        val btnChangeAccess = dialogView.findViewById<TextView>(R.id.accessButton)

        btnDeleteUser.setOnClickListener {
            deleteUserDialog(userName, userEmail)
        }
        btnChangeAccess.setOnClickListener {
            changeAccessDialog(userName, userEmail)
        }
    }

    private fun changeAccessDialog(userName: String, userEmail: String) {
        TODO("Not yet implemented")
    }

    private fun deleteUserDialog(userName: String, userEmail: String) {
        TODO("Not yet implemented")
    }
}