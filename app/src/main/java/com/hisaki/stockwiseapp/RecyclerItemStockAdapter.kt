package com.hisaki.stockwiseapp
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RecyclerItemStockAdapter(private val fragment: Fragment) : RecyclerView.Adapter<RecyclerItemStockAdapter.MyViewHolder>() {
    private var firestoreItemList = mutableListOf<ItemStock>()

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val itemCollection = firestoreDB.collection("Product")

    init {
        fetchData()
    }

    private fun fetchData() {
        itemCollection.orderBy("id", Query.Direction.DESCENDING)
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
                Toast.makeText(fragment.requireContext(), "Failed to fetch data: $exception", Toast.LENGTH_LONG).show()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemstock_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = firestoreItemList[position]
        holder.tvItemTitle.text = item.name
        holder.tvItemBarcode.text = item.barcode
        holder.tvItemPrice.text = "Price: ${item.price}"
        holder.tvItemStock.text = "Stock: ${item.stock}"

        holder.cardView.setOnClickListener {
            Toast.makeText(
                fragment.requireContext(),
                item.name,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return firestoreItemList.size
    }

    fun setData(itemList: MutableList<ItemStock>) {
        firestoreItemList.clear() // Clear the existing list
        firestoreItemList.addAll(itemList) // Add new items to the list
        notifyDataSetChanged() // Refresh the view after data is set
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemTitle: TextView = itemView.findViewById(R.id.itemBarcode)
        val tvItemBarcode: TextView = itemView.findViewById(R.id.itemName)
        val tvItemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val tvItemStock: TextView = itemView.findViewById(R.id.itemStock)
        val cardView: CardView = itemView.findViewById(R.id.cardViewStock)
    }
}
