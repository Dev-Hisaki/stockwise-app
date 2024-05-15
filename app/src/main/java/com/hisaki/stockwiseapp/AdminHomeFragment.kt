package com.hisaki.stockwiseapp

import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
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

    private fun detailTransactionDialog(
        productName: String,
        productId: Int,
        date: String,
        quantity: Int,
        leftover: Int,
        totalAmount: Double
    ) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(R.layout.popup_detail_transaksi)
        val dialog = dialogBuilder.create()

        dialog.setOnShowListener {
            val popupProductName = dialog.findViewById<TextView>(R.id.productNameTextView)
            val popupProductId = dialog.findViewById<TextView>(R.id.productIdValueTextView)
            val popupDate = dialog.findViewById<TextView>(R.id.dateValueTextView)
            val popupQuantity = dialog.findViewById<TextView>(R.id.quantityValueTextView)
            val popupLeftover = dialog.findViewById<TextView>(R.id.leftoverValueTextView)
            val popupTotalAmount = dialog.findViewById<TextView>(R.id.totalAmountValueTextView)

            // Set text values if views are not null
            popupProductName?.text = productName
            popupProductId?.text = productId.toString()
            popupDate?.text = date
            popupQuantity?.text = quantity.toString()
            popupLeftover?.text = leftover.toString()
            popupTotalAmount?.text = "Rp." + "%.2f".format(totalAmount)
        }
        dialog.show()
    }

    private fun getFormattedDate(): String {
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

        // Initialize adapters
        stokKeluarAdapter = StokKeluarAdapter(this, itemListStokKeluar)
        stokMasukAdapter = StokMasukAdapter(this, itemListStokMasuk)

        // Set layout managers and adapters for RecyclerViews
        recyclerViewStokKeluar = view.findViewById(R.id.rvItemStokKeluar)
        recyclerViewStokKeluar.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewStokKeluar.adapter = stokKeluarAdapter

        recyclerViewStokMasuk = view.findViewById(R.id.rvItemStokMasuk)
        recyclerViewStokMasuk.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewStokMasuk.adapter = stokMasukAdapter

        stokKeluarAdapter.setOnItemClickListener { stokKeluarData ->
            detailTransactionDialog(
                stokKeluarData.productName,
                stokKeluarData.productId,
                stokKeluarData.date,
                stokKeluarData.quantity,
                stokKeluarData.leftover,
                stokKeluarData.totalAmount
            )
        }

        stokMasukAdapter.setOnItemClickListener { stokMasukData ->
            detailTransactionDialog(
                stokMasukData.productName,
                stokMasukData.productId,
                stokMasukData.date,
                stokMasukData.quantity,
                stokMasukData.leftover,
                stokMasukData.totalAmount
            )
        }

        val stockInRef = FirebaseFirestore.getInstance().collection("Transaction")
            .whereEqualTo("type", TransactionRepository.TransactionType.IN)
        stockInRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val transactionData = document.data
                val id = document.id
                val type = transactionData["type"] as String
                val productId = (transactionData["productid"] as Long?)?.toInt() ?: 0
                val productName = transactionData["productname"] as String
                val productPrice = (transactionData["productprice"] as Double?) ?: 0.0
                val leftover = (transactionData["leftover"] as Long?)?.toInt() ?: 0
                val quantity = (transactionData["quantity"] as Long?)?.toInt() ?: 0
                val totalAmount = (transactionData["totalamount"] as Double?) ?: 0.0
                val timestamp = transactionData["date"] as Timestamp
                val date = timestamp.toDate()
                val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val formattedDate = simpleDateFormat.format(date)

                val stokMasukData = StokMasukData(
                    id = id.toInt(),
                    type = type,
                    productId = productId,
                    productName = productName,
                    productPrice = productPrice,
                    leftover = leftover,
                    quantity = quantity,
                    totalAmount = totalAmount,
                    date = formattedDate
                )
                itemListStokMasuk.add(stokMasukData)
            }

            banyakStokMasuk.text = itemListStokMasuk.size.toString()
            stokMasukAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.e("AdminHomeFragment", "Error getting stock in data", exception)
        }

        val stockOutRef = FirebaseFirestore.getInstance().collection("Transaction")
            .whereEqualTo("type", TransactionRepository.TransactionType.OUT)
        stockOutRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val transactionData = document.data
                val id = document.id
                val type = transactionData["type"] as String
                val productId = (transactionData["productid"] as Long?)?.toInt() ?: 0
                val productName = transactionData["productname"] as String
                val productPrice = (transactionData["productprice"] as Double?) ?: 0.0
                val leftover = (transactionData["leftover"] as Long?)?.toInt() ?: 0
                val quantity = (transactionData["quantity"] as Long?)?.toInt() ?: 0
                val totalAmount = (transactionData["totalamount"] as Double?) ?: 0.0
                val timestamp = transactionData["date"] as Timestamp
                val date = timestamp.toDate()
                val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val formattedDate = simpleDateFormat.format(date)

                val stokKeluarData = StokKeluarData(
                    id = id.toInt(),
                    type = type,
                    productId = productId,
                    productName = productName,
                    productPrice = productPrice,
                    leftover = leftover,
                    quantity = quantity,
                    totalAmount = totalAmount,
                    date = formattedDate
                )
                itemListStokKeluar.add(stokKeluarData)
            }

            banyakStokKeluar.text = itemListStokKeluar.size.toString()
            stokKeluarAdapter.notifyDataSetChanged()
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
