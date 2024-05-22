package com.hisaki.stockwiseapp

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Double): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(amount)
}
