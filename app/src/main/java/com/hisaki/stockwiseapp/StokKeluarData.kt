package com.hisaki.stockwiseapp

data class StokKeluarData(
    val id: Int,
    val type: String,
    val productName: String,
    val productPrice: Double,
    val quantity: Int,
    val totalAmount: Double
)
