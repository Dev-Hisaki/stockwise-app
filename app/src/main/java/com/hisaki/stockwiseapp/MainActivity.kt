package com.hisaki.stockwiseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    /*private lateinit var bottomNavigation: BottomNavigationView*/
    private lateinit var time: TextView
    private lateinit var email: TextView
    private lateinit var receivedEmail: String
    /*private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: StokMasukAdapter*/

    fun getFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
    private fun initCommponent() {
        /*val itemList = listOf(
            // Laptop
            StokMasukData(1, "Laptop", "ASUS Vivobook 15", "Laptop 15 inci dengan prosesor Intel Core i3", 5000000.0),
            StokMasukData(5, "Laptop", "Acer Aspire 5", "Laptop 15 inci dengan RAM 8GB dan SSD 256GB", 6000000.0),
            StokMasukData(7, "Laptop", "HP Pavilion 14", "Laptop 14 inci dengan desain tipis dan ringan", 5500000.0),
            StokMasukData(9, "Laptop", "MacBook Air M1", "Laptop Apple dengan chip M1 yang powerful dan hemat daya", 14000000.0),
            StokMasukData(11, "Laptop", "ASUS ROG Zephyrus G14", "Laptop gaming dengan desain tipis dan performa tinggi", 20000000.0),
            StokMasukData(13, "Laptop", "Lenovo Legion 5", "Laptop gaming dengan layar 144Hz dan performa RTX 3050", 13000000.0),
            StokMasukData(15, "Laptop", "MSI GF63 Thin", "Laptop gaming dengan desain tipis dan harga terjangkau", 8000000.0),
            StokMasukData(17, "Laptop", "Acer Nitro 5", "Laptop gaming dengan performa AMD Ryzen 5 dan Radeon RX 6600M", 10000000.0),
            StokMasukData(19, "Laptop", "ASUS TUF Dash F15", "Laptop gaming dengan desain ramping dan performa RTX 3060", 14000000.0),

            // Smartphone
            StokMasukData(2, "Smartphone", "Samsung Galaxy A53 5G", "Smartphone 5G dengan kamera 64MP", 6500000.0),
            StokMasukData(4, "Smartphone", "Xiaomi Redmi Note 11 Pro", "Smartphone dengan layar AMOLED dan fast charging", 4200000.0),
            StokMasukData(6, "Smartphone", "Realme 9 Pro+", "Smartphone dengan kamera 50MP dan pengisian daya 60W", 4500000.0),
            StokMasukData(8, "Smartphone", "Vivo V23 5G", "Smartphone dengan kamera selfie 50MP dan desain ramping", 6000000.0),
            StokMasukData(10, "Smartphone", "iPhone 13", "Smartphone Apple dengan kamera terbaik dan performa tangguh", 12000000.0),
            StokMasukData(12, "Smartphone", "Samsung Galaxy S22 Ultra", "Smartphone flagship Samsung dengan kamera zoom 100x", 17000000.0),
            StokMasukData(14, "Smartphone", "Xiaomi 12 Pro", "Smartphone dengan layar AMOLED 120Hz dan chipset Snapdragon 8 Gen 1", 9000000.0),
            StokMasukData(16, "Smartphone", "OPPO Find X5 Pro", "Smartphone dengan desain premium dan kamera Hasselblad", 12000000.0),
            StokMasukData(18, "Smartphone", "Realme GT Neo 3", "Smartphone dengan pengisian daya 150W tercepat di dunia", 6500000.0),
            StokMasukData(20, "Smartphone", "Vivo X80 Pro", "Smartphone dengan kamera Zeiss dan chip Dimensity 9000", 11000000.0)
        )
        bottomNavigation = findViewById(R.id.bottomNavigationView)*/
        time = findViewById(R.id.time)
        email = findViewById(R.id.email)
        receivedEmail = intent.getStringExtra("EMAIL").toString()
        /*recyclerView = findViewById(R.id.recyclerView)
        itemAdapter = StokMasukAdapter(itemList)*/

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCommponent()
        time.text = "Recap, ${getFormattedDate()}"
        email.text = "Welcome Back, $receivedEmail!"
        /*recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = itemAdapter

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    Log.i("INGFO", "INI HOME")
                    true
                }
                R.id.navigation_scanqr -> {
                    Log.i("INGFO", "INI SCANQR")
                    true
                }
                R.id.navigation_stock -> {
                    Log.i("INGFO", "INI STOCK")
                    true
                }
                else -> false
            }
        }*/
    }
}