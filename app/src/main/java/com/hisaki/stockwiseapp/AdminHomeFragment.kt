package com.hisaki.stockwiseapp

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminHomeFragment : Fragment() {

    private lateinit var recyclerViewStokKeluar: RecyclerView
    private lateinit var recyclerViewStokMasuk: RecyclerView
    private lateinit var stokKeluarAdapter: StokKeluarAdapter
    private lateinit var stokMasukAdapter: StokMasukAdapter
    private lateinit var email: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var userEmail: String? = null
    private lateinit var time: TextView
    private lateinit var banyakStokKeluar: TextView
    private lateinit var banyakStokMasuk: TextView
    private lateinit var navigateToProfileActivity: ImageView
    private var itemListStokKeluar = mutableListOf<StokKeluarData>()
    private var itemListStokMasuk = mutableListOf<StokMasukData>()

    fun getFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        val stockInRef = FirebaseFirestore.getInstance().collection("Transaction").whereEqualTo("type", TransactionRepository.TransactionType.IN)
        stockInRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val transactionData = document.data
                val id = document.id
                val type = transactionData["type"] as String
                val productName = transactionData["productname"] as String
                val productPrice = (transactionData["productprice"] as Double?) ?: 0.0
                val quantity = (transactionData["quantity"] as Long?)?.toInt() ?: 0

                val totalAmount = (transactionData["totalamount"] as Double?) ?: 0.0

                val stokMasukData = StokMasukData(
                    id = id.toInt(),
                    type = type,
                    productName = productName,
                    productPrice = productPrice,
                    quantity = quantity,
                    totalAmount = totalAmount
                )
                itemListStokMasuk.add(stokMasukData)
            }

            // Set adapter RecyclerView dengan data yang sudah ada
            stokMasukAdapter = StokMasukAdapter(this@AdminHomeFragment, itemListStokMasuk)
            recyclerViewStokMasuk.adapter = stokMasukAdapter
            banyakStokMasuk.text = itemListStokMasuk.size.toString()
        }.addOnFailureListener { exception ->
            Log.e("AdminHomeFragment", "Error getting stock in data", exception)
        }

        val stockOutRef = FirebaseFirestore.getInstance().collection("Transaction").whereEqualTo("type", TransactionRepository.TransactionType.OUT)
        stockOutRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val transactionData = document.data
                val id = document.id
                val type = transactionData["type"] as String
                val productName = transactionData["productname"] as String
                val productPrice = (transactionData["productprice"] as Double?) ?: 0.0
                val quantity = (transactionData["quantity"] as Long?)?.toInt() ?: 0
                val totalAmount = (transactionData["totalamount"] as Double?) ?: 0.0

                val stokKeluarData = StokKeluarData(
                    id = id.toInt(),
                    type = type,
                    productName = productName,
                    productPrice = productPrice,
                    quantity = quantity,
                    totalAmount = totalAmount
                )
                itemListStokKeluar.add(stokKeluarData)
            }

            // Set adapter RecyclerView dengan data yang sudah ada
            stokKeluarAdapter = StokKeluarAdapter(this@AdminHomeFragment, itemListStokKeluar)
            recyclerViewStokKeluar.adapter = stokKeluarAdapter
            banyakStokKeluar.text = itemListStokKeluar.size.toString()
        }.addOnFailureListener { exception ->
            Log.e("AdminHomeFragment", "Error getting stock out data", exception)
        }

        sharedPreferences = requireActivity().getSharedPreferences("shared_pref", MODE_PRIVATE)
        userEmail = sharedPreferences.getString("Email", null)

        email = view.findViewById(R.id.email)
        time = view.findViewById(R.id.time)
        banyakStokKeluar = view.findViewById(R.id.banyakStokKeluar)
        banyakStokMasuk = view.findViewById(R.id.banyakStokMasuk)
        navigateToProfileActivity = view.findViewById(R.id.navigateToProfileActivity)

        itemListStokKeluar = ArrayList()
        recyclerViewStokKeluar = view.findViewById(R.id.rvItemStokKeluar)
        recyclerViewStokKeluar.layoutManager = LinearLayoutManager(requireContext())
        stokKeluarAdapter = StokKeluarAdapter(this, itemListStokKeluar)

        itemListStokMasuk = ArrayList()
        recyclerViewStokMasuk = view.findViewById(R.id.rvItemStokMasuk)
        recyclerViewStokMasuk.layoutManager = LinearLayoutManager(requireContext())
        stokMasukAdapter = StokMasukAdapter(this, itemListStokMasuk)

        recyclerViewStokKeluar.adapter = stokKeluarAdapter
        recyclerViewStokMasuk.adapter = stokMasukAdapter

        userEmail?.let {
            time.text = "Recap, ${getFormattedDate()}"
            email.text = "Welcome Back, $it!"
        }

        navigateToProfileActivity.setOnClickListener(View.OnClickListener {
            val intentToProfileActivity = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intentToProfileActivity)
        })
        return view
    }
}