package com.hisaki.stockwiseapp

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminAdapter(
    private val itemList: MutableList<AdminData>
) : RecyclerView.Adapter<AdminAdapter.ItemViewHolder>() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("User")

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
            detailUserDialog(itemData.name, itemData.email, holder.itemView.context, position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun detailUserDialog(
        userName: String,
        userEmail: String,
        context: Context,
        position: Int
    ) {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.popup_detail_kelola_akun_admin, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
        val dialog = dialogBuilder.create()
        dialog.show()

        dialogView.findViewById<TextView>(R.id.userNameTextView)?.text = "Admin $userName"

        val btnChangeAccess = dialogView.findViewById<TextView>(R.id.accessButton)

        btnChangeAccess.setOnClickListener {
            changeAccessDialog(userName, userEmail, context, position)
        }
    }

    private fun changeAccessDialog(
        userName: String,
        userEmail: String,
        context: Context,
        position: Int
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.popup_ubah_hak_akses, null)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()

        val accessTextView = dialogView.findViewById<TextView>(R.id.accessTextView)
        val changeAdminButton = dialogView.findViewById<Button>(R.id.changeAdminButton)
        val cancelAccessButton = dialogView.findViewById<Button>(R.id.cancelAccessButton)

        accessTextView.text =
            "Apakah anda yakin ingin mengubah hak akses $userName menjadi user?"

        changeAdminButton.setOnClickListener {
            val query = collectionRef.whereEqualTo("email", userEmail)

            query.get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentId = document.id

                    val updates = hashMapOf<String, Any>(
                        "role" to "user"
                    )

                    collectionRef.document(documentId)
                        .update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Hak akses user berhasil diubah",
                                Toast.LENGTH_SHORT
                            ).show()

                            deleteItem(position)

                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Gagal mengubah hak akses",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }.addOnFailureListener { e ->
                Log.e("UpdateUserRole", "Gagal mendapatkan user", e)
            }
        }

        cancelAccessButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }
}