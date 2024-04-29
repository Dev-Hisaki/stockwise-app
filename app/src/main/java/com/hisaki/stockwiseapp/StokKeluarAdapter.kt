package com.hisaki.stockwiseapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class StokKeluarAdapter(
    private val fragment: Fragment,
    private val itemList: List<StokKeluarData>
) : RecyclerView.Adapter<StokKeluarAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleStokKeluarTextView)
        val idTextView: TextView = itemView.findViewById(R.id.idStokKeluarTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceStokKeluarTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stok_keluar_item_layout, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemData = itemList[position]
        holder.titleTextView.text = itemData.title
        holder.idTextView.text = "00" + itemData.id.toString()
        holder.priceTextView.text = "Rp." + "%.2f".format(itemData.price)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}