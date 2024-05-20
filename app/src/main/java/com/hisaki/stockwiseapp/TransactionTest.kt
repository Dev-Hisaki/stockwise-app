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
    }

    private fun addTransaction() {
        val newTransaction = Transaction(
            username = "admin",
            productid = "a",
            productname = "Permen",
            productprice = 500.00,
            productstock = 99,
            quantity = 76,
            type = TransactionRepository.TransactionType.OUT,
            date = Date()
        )
        transactionRepository.addTransaction(newTransaction)
    }

}
