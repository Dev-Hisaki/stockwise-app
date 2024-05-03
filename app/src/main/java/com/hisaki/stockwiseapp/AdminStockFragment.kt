package com.hisaki.stockwiseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdminStockFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerItemStockAdapter: RecyclerItemStockAdapter
    private val firestoreDb = FirebaseFirestore.getInstance()
    private val itemCollection = firestoreDb.collection("Product")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_stock, container, false)

        recyclerView = view.findViewById(R.id.rvItemStock)
        // Inisialisasi adapter dengan fragment ini
        recyclerItemStockAdapter = RecyclerItemStockAdapter(this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerItemStockAdapter

        fetchData()

        return view
    }

    private fun fetchData() {
        val itemList = mutableListOf<ItemStock>()
        itemCollection.orderBy("id", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val barcode = document.getString("barcode") ?: ""
                    val id = document.getLong("id")?.toInt() ?: 0
                    val name = document.getString("name") ?: ""
                    val price = document.getLong("price") ?: 0
                    val stock = document.getString("stock") ?: ""

                    val item = ItemStock(barcode, id, name, price, stock)
                    itemList.add(item)
                }

                recyclerItemStockAdapter.setData(itemList)
            }
            .addOnFailureListener { exception ->
            }
    }
}
