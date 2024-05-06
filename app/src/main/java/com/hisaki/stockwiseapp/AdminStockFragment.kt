package com.hisaki.stockwiseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdminStockFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerItemStockAdapter: RecyclerItemStockAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: androidx.appcompat.widget.SearchView

    private val firestoreDb = FirebaseFirestore.getInstance()
    private val itemCollection = firestoreDb.collection("Product")

    private var originalItemList = mutableListOf<ItemStock>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_stock, container, false)

        swipeRefreshLayout = view.findViewById(R.id.refreshstock)
        recyclerView = view.findViewById(R.id.rvItemStock)
        recyclerItemStockAdapter = RecyclerItemStockAdapter(this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerItemStockAdapter

        searchView = view.findViewById(R.id.scvrv)

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchItem(it)
                }
                return true
            }
        })

        fetchData()

        swipeRefreshLayout.setOnRefreshListener {
            fetchData()
        }

        return view
    }

    private fun fetchData() {
        itemCollection.orderBy("id", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemStock>()
                for (document in result) {
                    val barcode = document.getString("barcode") ?: ""
                    val id = document.getLong("id")?.toInt() ?: 0
                    val img = document.getString("img") ?: ""
                    val name = document.getString("name") ?: ""
                    val price = document.getLong("price") ?: 0
                    val stock = document.getString("stock") ?: ""

                    val item = ItemStock(barcode, id, img, name, price, stock)
                    itemList.add(item)
                }

                originalItemList = itemList.toMutableList()
                recyclerItemStockAdapter.setData(itemList)
                swipeRefreshLayout.isRefreshing = false
            }
            .addOnFailureListener { exception ->
                swipeRefreshLayout.isRefreshing = false
            }
    }

    private fun searchItem(query: String) {
        val filteredList = originalItemList.filter {
            it.name!!.contains(query, ignoreCase = true) || it.barcode!!.contains(query, ignoreCase = true)
        }
        recyclerItemStockAdapter.setData(filteredList.toMutableList())
    }
}
