package com.hisaki.stockwiseapp

import TransactionRepository
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Date

class AdminStokBarang : AppCompatActivity() {
    private val transactionRepository = TransactionRepository()
    private lateinit var sharedPreferences: SharedPreferences
    private var userName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()*/
        setContentView(R.layout.activity_admin_stok_barang)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.RelativeLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        val id = intent.getStringExtra("id")
        val img = intent.getStringExtra("img")
        val barcode = intent.getStringExtra("barcode")
        val name = intent.getStringExtra("name")
        val price = intent.getLongExtra("price", 0)
        val stock = intent.getStringExtra("stock")

        val ivItemImg: ImageView = findViewById(R.id.ivItemImg)
        val tvItemBarcode : TextView = findViewById(R.id.tvItemBarcode)
        val tvItemName : TextView = findViewById(R.id.tvItemName)
        val tvItemPrice : TextView = findViewById(R.id.tvItemPrice)
        val tvItemStock : TextView = findViewById(R.id.tvItemStock)

        Glide.with(this).load(img).into(ivItemImg)
        tvItemBarcode.text = barcode
        tvItemName.text = name
        tvItemPrice.text = "$price"
        tvItemStock.text = stock.toString()

        var stokSekarang = "0"

        val backbtn : ImageView = findViewById(R.id.ivbackbtn)
        val tambahStokButton : Button = findViewById(R.id.tambahStokButton)
        val kurangStokButton : Button = findViewById(R.id.kurangiStokButton)

        fun showBottomSheetDialog(type: String, name: String?, stock: String?) {
            sharedPreferences = this.getSharedPreferences("shared_pref", MODE_PRIVATE)
            userName = sharedPreferences.getString("Username", null)
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetView: View = if (type == "tambah") {
                layoutInflater.inflate(R.layout.activity_popup_tambah_stok_barang_admin, null)
            } else {
                layoutInflater.inflate(R.layout.activity_popup_kurangi_stok_barang_admin, null)
            }

            val namaBarang: TextView = bottomSheetView.findViewById(
                if (type == "tambah") R.id.namaBarangTambahStokTextView else R.id.namaBarangKurangiStokTextView
            )
            val stokBarang: TextView = bottomSheetView.findViewById(
                if (type == "tambah") R.id.stokBarangTambahStokTextView else R.id.stokBarangKurangiStokTextView
            )
            val ubahStokBarang: TextView = bottomSheetView.findViewById(
                if (type == "tambah") R.id.ubahStokTambahStokTextView else R.id.ubahStokKurangiStokTextView
            )
            val tambahButton: AppCompatButton = bottomSheetView.findViewById(
                if (type == "tambah") R.id.tambahAngkaTambahStokButton else R.id.tambahAngkaKurangiStokButton
            )
            val kurangButton: AppCompatButton = bottomSheetView.findViewById(
                if (type == "tambah") R.id.kurangAngkaTambahStokButton else R.id.kurangAngkaKurangiStokButton
            )
            val confirmButton: AppCompatButton = bottomSheetView.findViewById(
                if (type == "tambah") R.id.confirmTambahStokTextView else R.id.confirmKurangStokTextView
            )


            namaBarang.text = name
            stokBarang.text = stock
            ubahStokBarang.text = "0"

            tambahButton.setOnClickListener {
                val value = ubahStokBarang.text.toString().toLong() + 1
                ubahStokBarang.text = value.toString()
                stokSekarang = value.toString()
                Log.e("test", value.toString())
            }

            kurangButton.setOnClickListener {
                val value = ubahStokBarang.text.toString().toLong() - 1
                if (value >= 0) {
                    ubahStokBarang.text = value.toString()
                    stokSekarang = value.toString()
                }
            }

            confirmButton.setOnClickListener {
                val newTransaction = Transaction(
                    username = userName.toString(),
                    productid = barcode.toString(),
                    productname = name.toString(),
                    productprice = price.toDouble(),
                    productstock = stock!!.toInt(),
                    quantity = if (confirmButton.id == R.id.confirmTambahStokTextView) stokSekarang!!.toLong().toInt() else stokSekarang!!.toLong().toInt(),
                    type = if (confirmButton.id == R.id.confirmTambahStokTextView) TransactionRepository.TransactionType.IN else TransactionRepository.TransactionType.OUT,
                    date = Date()
                )
                transactionRepository.addTransaction(newTransaction)
                Toast.makeText(
                    this@AdminStokBarang,
                    if (confirmButton.id == R.id.confirmTambahStokTextView) "Stok berhasil ditambahkan!" else "Stok berhasil dikurangi!",
                    Toast.LENGTH_SHORT
                ).show()
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

        backbtn.setOnClickListener{
            onBackPressed()
        }

        tambahStokButton.setOnClickListener {
            showBottomSheetDialog("tambah", name, stock)
        }

        kurangStokButton.setOnClickListener {
            showBottomSheetDialog("kurang", name, stock)
        }
    }
}