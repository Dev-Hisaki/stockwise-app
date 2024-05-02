package com.hisaki.stockwiseapp

import java.util.Date

data class Transaction (
    val userid: Int,
    val username: String,
    val productid: Int,
    val productname: String,
    val productprice: Double,
    val productstock: Int,
    val quantity: Int,
    val totalamount: Double = productprice * quantity,
    val leftover: Int,
    val type: TransactionRepository.TransactionType,
    val date: Date
)