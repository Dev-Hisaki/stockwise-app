package com.hisaki.stockwiseapp
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        holder.tvItemBarcode.text = item.barcode
        holder.tvItemTitle.text = item.name
        holder.tvItemPrice.text = "Harga: ${formatRupiah(item.price!!.toDouble())}"
        holder.tvItemStock.text = "Stok: ${item.stock}"

        Glide.with(fragment)
            .load(item.img)
            .into(holder.ivItemImg)

        holder.cardView.setOnClickListener {
            val context = fragment.requireContext()
            val intent = Intent(context, AdminStokBarang::class.java)
            intent.putExtra("id", item.id)
            intent.putExtra("img",item.img)
            intent.putExtra("barcode",item.barcode)
            intent.putExtra("name",item.name)
            intent.putExtra("price",item.price)
            intent.putExtra("stock",item.stock)
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener{
            view -> showPopup(view, position)
            true
        }
    }

    private fun showPopup(view: View, position: Int) {
        val item = firestoreItemList[position]
        val dialogView = LayoutInflater.from(view.context).inflate(R.layout.activity_popup_produk_admin, null)
        val editstock: TextView = dialogView.findViewById(R.id.editstock)
        val deleteBtn: TextView = dialogView.findViewById(R.id.delbtn)
        val tvproduct: TextView = dialogView.findViewById(R.id.tvproduk)

        tvproduct.setText(item.name)

        val alertDialog = AlertDialog.Builder(view.context)
            .setView(dialogView)
            .create()

        editstock.setOnClickListener {
            alertDialog.dismiss()
            val context = fragment.requireContext()
            val intent = Intent(context, AdminStokBarang::class.java)
            intent.putExtra("id", item.id)
            intent.putExtra("img", item.img)
            intent.putExtra("barcode", item.barcode)
            intent.putExtra("name", item.name)
            intent.putExtra("price", item.price)
            intent.putExtra("stock", item.stock)
            fragment.startActivityForResult(intent, 1)
        }


        deleteBtn.setOnClickListener {
            alertDialog.dismiss()
            deleteItem(item.id.toString(), position)
            Toast.makeText(view.context, "Item ${item.name} deleted", Toast.LENGTH_SHORT).show()
        }

        alertDialog.show()
    }

    private fun deleteItem(itemId: String, position: Int){
        itemCollection.document(itemId).delete().addOnSuccessListener {
            firestoreItemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, firestoreItemList.size)
        }
            .addOnFailureListener{
                exception -> Toast.makeText(fragment.requireContext(), "Error deleting item: $exception", Toast.LENGTH_LONG).show()
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
        val tvItemBarcode: TextView = itemView.findViewById(R.id.itemBarcode)
        val tvItemTitle: TextView = itemView.findViewById(R.id.itemName)
        val tvItemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val tvItemStock: TextView = itemView.findViewById(R.id.itemStock)
        val ivItemImg: ImageView = itemView.findViewById(R.id.itemImg)
        val cardView: CardView = itemView.findViewById(R.id.cardViewStock)
    }
}
