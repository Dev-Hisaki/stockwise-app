package com.hisaki.stockwiseapp

import java.util.Date

data class Transaction (
    var id: Int? = null,
    val username: String,
    val productid: String,
    val productname: String,
    val productprice: Double,
    val productstock: Int,
    val quantity: Int,
    val totalamount: Double = productprice * quantity,
    var leftover: Int,
    val type: TransactionRepository.TransactionType,
    val date: Date
) {
    constructor(
        username: String,
        productid: String,
        productname: String,
        productprice: Double,
        productstock: Int,
        quantity: Int,
        type: TransactionRepository.TransactionType,
        date: Date
    ) : this(
        null,
        username,
        productid,
        productname,
        productprice,
        productstock,
        quantity,
        productprice * quantity,
        productstock,
        type,
        date
    )}

