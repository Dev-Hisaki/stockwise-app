package com.hisaki.stockwiseapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemStockAdapter constructor(
    private val fragment: Fragment,
    private val itemList: List<ItemStock>
) :
    RecyclerView.Adapter<RecyclerItemStockAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.itemstock_list, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvItemTitle.text = itemList[position].title
        holder.ivItemImg.setImageResource(itemList[position].image)

        holder.cardView.setOnClickListener {
            Toast.makeText(
                fragment.requireContext(),
                itemList[position].title,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        val ivItemImg: ImageView = itemView.findViewById(R.id.itemImg)
        val cardView: CardView = itemView.findViewById(R.id.cardViewStock)
    }

}

