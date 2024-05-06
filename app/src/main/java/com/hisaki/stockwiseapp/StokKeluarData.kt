package com.hisaki.stockwiseapp

data class StokKeluarData(
    val id: Int,
    val type: String,
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    val leftover: Int,
    val quantity: Int,
    val totalAmount: Double,
    val date: String
)
