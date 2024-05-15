package com.hisaki.stockwiseapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class AdminStokBarang : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_stok_barang)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.RelativeLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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

        val backbtn : ImageView = findViewById(R.id.ivbackbtn)
        backbtn.setOnClickListener{
            onBackPressed()
        }

    }
}