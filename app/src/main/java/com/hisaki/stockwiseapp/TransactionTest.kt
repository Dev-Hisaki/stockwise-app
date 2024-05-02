package com.hisaki.stockwiseapp

import TransactionRepository
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class TransactionTest: AppCompatActivity() {

    private val transactionRepository = TransactionRepository()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        addTransaction()
        getTransactions()
    }

    private fun addTransaction() {
        val newTransaction = Transaction(
            userid = 1,
            username = "admin",
            productid = 1,
            productname = "Kopi Susu Kapal Api",
            productprice = 2500.00,
            productstock = 99,
            quantity = 2,
            leftover = 97,
            type = TransactionRepository.TransactionType.OUT,
            date = Date()
        )
        transactionRepository.addTransaction(newTransaction)
    }

    private fun getTransactions() {
        val transactionRef = db.collection("Transaction")
        transactionRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("MainActivity", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents: ", exception)
            }
    }
}