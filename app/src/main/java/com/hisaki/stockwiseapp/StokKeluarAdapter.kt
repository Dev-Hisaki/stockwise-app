package com.hisaki.stockwiseapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class StokKeluarAdapter(
    private val fragment: Fragment,
    private val itemList: MutableList<StokKeluarData>
) : RecyclerView.Adapter<StokKeluarAdapter.ItemViewHolder>() {
    private var onItemClickListener: ((StokKeluarData) -> Unit)? = null

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleStokKeluarTextView)
        val idTextView: TextView = itemView.findViewById(R.id.idStokKeluarTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityStokKeluar)
        val totalAmountTextView: TextView = itemView.findViewById(R.id.totalAmountStokKeluarTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stok_keluar_item_layout, parent, false)
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
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setOnItemClickListener(listener: (StokKeluarData) -> Unit) {
        this.onItemClickListener = listener
    }
}