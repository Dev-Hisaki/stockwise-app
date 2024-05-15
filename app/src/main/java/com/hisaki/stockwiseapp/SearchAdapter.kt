package com.hisaki.stockwiseapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SearchAdapter(private val context: Context) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val srbarcode: TextView = itemView.findViewById(R.id.srcbarcode)
        val srtitle: TextView = itemView.findViewById(R.id.srctitle)
        val srprice: TextView = itemView.findViewById(R.id.srcprice)
        val srstock: TextView = itemView.findViewById(R.id.srcstock)
        val srimage: ImageView = itemView.findViewById(R.id.srcimage)
    }

    private var firestoreItemList = mutableListOf<ItemStock>()

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val itemCollection = firestoreDB.collection("Product")

    init {
        fetchData()
    }

    private fun fetchData() {
        itemCollection.orderBy("id", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val item = document.toObject(ItemStock::class.java)
                    firestoreItemList.add(item)
                }
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(context, "Failed to fetch data: $exception", Toast.LENGTH_LONG)
                    .show()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemstocksearch_list, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = firestoreItemList[position]
        holder.srbarcode.text = item.barcode
        holder.srtitle.text = item.name
        holder.srprice.text = "Rp. ${item.price}"
        holder.srstock.text = item.stock

        Glide.with(context)
            .load(item.img)
            .into(holder.srimage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, AdminStokBarang::class.java)
            intent.putExtra("img", item.img)
            intent.putExtra("barcode", item.barcode)
            intent.putExtra("name", item.name)
            intent.putExtra("price", item.price)
            intent.putExtra("stock", item.stock)
            context.startActivity(intent)
        }
    }

    fun setData(itemList: MutableList<ItemStock>) {
        firestoreItemList.clear() // Clear the existing list
        firestoreItemList.addAll(itemList) // Add new items to the list
        notifyDataSetChanged() // Refresh the view after data is set
    }

    override fun getItemCount(): Int {
        return firestoreItemList.size
    }
}
