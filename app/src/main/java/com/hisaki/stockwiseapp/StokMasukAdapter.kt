package com.hisaki.stockwiseapp

import android.content.Context
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class StokMasukAdapter(
    private val fragment: Fragment,
    private val itemList: MutableList<StokMasukData>
) : RecyclerView.Adapter<StokMasukAdapter.ItemViewHolder>() {
    private var onItemClickListener: ((StokMasukData) -> Unit)? = null

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleStokMasukTextView)
        val idTextView: TextView = itemView.findViewById(R.id.idStokMasukTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityStokMasuk)
        val totalAmountTextView: TextView = itemView.findViewById(R.id.totalAmountStokMasukTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stok_masuk_item_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemData = itemList[position]
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(itemData)
        }
        holder.titleTextView.text = itemData.productName
        holder.idTextView.text = "00" + itemData.id.toString()
        holder.quantityTextView.text = itemData.quantity.toString()
        holder.totalAmountTextView.text = formatRupiah(itemData.totalAmount)
        holder.itemView.setOnLongClickListener {
            showPopup(it.context, it)
            true
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setOnItemClickListener(listener: (StokMasukData) -> Unit) {
        this.onItemClickListener = listener
    }

    private fun showPopup(context: Context, anchorView: View) {
        val popupMenu = PopupMenu(context, anchorView)
        popupMenu.inflate(R.menu.bottom_navigation_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Tindakan saat menu item 1 diklik
                    true
                }

                R.id.navigation_scanqr -> {
                    // Tindakan saat menu item 2 diklik
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }
}
