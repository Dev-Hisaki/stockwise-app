package com.hisaki.stockwiseapp

data class ItemStock(
    var barcode: String ?= null,
    var id: Int ?= null,
    var name: String ?= null,
    var price: Long ?= null,
    var stock: String ?= null
)