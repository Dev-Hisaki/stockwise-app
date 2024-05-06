package com.hisaki.stockwiseapp

import java.util.Date

data class Transaction (
    var id: Int? = null,
    val userid: Int,
    val username: String,
    val productid: Int,
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
        userid: Int,
        username: String,
        productid: Int,
        productname: String,
        productprice: Double,
        productstock: Int,
        quantity: Int,
        type: TransactionRepository.TransactionType,
        date: Date
    ) : this(
        null,
        userid,
        username,
        productid,
        productname,
        productprice,
        productstock,
        quantity,
        productprice * quantity,
        if (type == TransactionRepository.TransactionType.IN) productstock + quantity else productstock - quantity,
        type,
        date
    )

    // Setter kustom untuk leftover
    // Dapat digunakan jika kamu ingin mengubah leftover setelah objek Transaksi dibuat
    fun setLeftover() {
        leftover = if (type == TransactionRepository.TransactionType.IN) productstock + quantity else productstock - quantity
    }
}
