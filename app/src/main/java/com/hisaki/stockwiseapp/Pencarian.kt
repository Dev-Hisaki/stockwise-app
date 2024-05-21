package com.hisaki.stockwiseapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Pencarian : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView

    private val firestoreDb = FirebaseFirestore.getInstance()
    private val itemCollection = firestoreDb.collection("Product")
    private var originalItemList = mutableListOf<ItemStock>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pencarian)

        swipeRefreshLayout = findViewById(R.id.refreshsearch)
        recyclerView = findViewById(R.id.scitemsearch)
        searchAdapter = SearchAdapter(this)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = searchAdapter

        searchView = findViewById(R.id.searcch)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        val backButton: ImageView = findViewById(R.id.backksearch)
        backButton.setOnClickListener {
            onBackPressed()
        }

        fetchData()

        swipeRefreshLayout.setOnRefreshListener {
            fetchData()
        }
    }

    private fun fetchData() {
        itemCollection.orderBy("id", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemStock>()
                for (document in result) {
                    val barcode = document.getString("barcode") ?: ""
                    val id = document.getString("id") ?: ""
                    val img = document.getString("img") ?: ""
                    val name = document.getString("name") ?: ""
                    val price = document.getLong("price") ?: 0
                    val stock = document.getString("stock") ?: ""

                    val item = ItemStock(barcode, id, img, name, price, stock)
                    itemList.add(item)
                }

                originalItemList = itemList.toMutableList()
                searchAdapter.setData(itemList)
                swipeRefreshLayout.isRefreshing = false
            }
            .addOnFailureListener { exception ->
                swipeRefreshLayout.isRefreshing = false
            }
    }

    private fun searchItem(query: String) {
        val filteredList = originalItemList.filter {
            it.name!!.contains(query, ignoreCase = true) || it.barcode!!.contains(
                query,
                ignoreCase = true
            )
        }
        searchAdapter.setData(filteredList.toMutableList())
    }
}
