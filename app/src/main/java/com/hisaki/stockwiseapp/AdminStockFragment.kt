package com.hisaki.stockwiseapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdminStockFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerItemStockAdapter: RecyclerItemStockAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val firestoreDb = FirebaseFirestore.getInstance()
    private val itemCollection = firestoreDb.collection("Product")
    private var originalItemList = mutableListOf<ItemStock>()

    private lateinit var sharedPreferences: SharedPreferences
    private var roleUser: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_stock, container, false)
        getSession()
        swipeRefreshLayout = view.findViewById(R.id.refreshstock)
        recyclerView = view.findViewById(R.id.rvItemStock)
        recyclerItemStockAdapter = RecyclerItemStockAdapter(this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerItemStockAdapter

        val materialCardView: MaterialCardView = view.findViewById(R.id.mcvrv)
        val linearLayout: LinearLayout = materialCardView.findViewById(R.id.llsearch)
        linearLayout.setOnClickListener {
            val intent = Intent(requireContext(), Pencarian::class.java)
            startActivity(intent)
        }

        val addBtn: ImageView = view.findViewById(R.id.addstock)
        if(roleUser == "admin"){
            addBtn.setOnClickListener {
                val intent = Intent(requireContext(), TambahProduk::class.java)
                startActivity(intent)
            }
        } else {
            addBtn.visibility = View.GONE
        }

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
                    val id = document.getString("id") ?: ""
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

    private fun getSession() {
        context?.let {
            sharedPreferences =
                it.getSharedPreferences("shared_pref", AppCompatActivity.MODE_PRIVATE)
            roleUser = sharedPreferences.getString("Role", null)
        }
    }
}
