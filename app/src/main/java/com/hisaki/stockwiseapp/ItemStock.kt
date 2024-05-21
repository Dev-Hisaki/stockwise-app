package com.hisaki.stockwiseapp

data class ItemStock(
    var barcode: String ?= null,
    var id: String ?= null,
    var img: String ?= null,
    var name: String ?= null,
    var price: Long ?= null,
    var stock: String ?= null
)