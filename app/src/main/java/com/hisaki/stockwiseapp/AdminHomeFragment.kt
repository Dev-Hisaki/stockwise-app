package com.hisaki.stockwiseapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminHomeFragment : Fragment() {

    private lateinit var recyclerViewStokKeluar: RecyclerView
    private lateinit var recyclerViewStokMasuk: RecyclerView
    private lateinit var stokKeluarAdapter: StokKeluarAdapter
    private lateinit var stokMasukAdapter: StokMasukAdapter
    private lateinit var email: TextView
    private lateinit var receivedEmail: String
    private lateinit var time: TextView
    private lateinit var banyakStokKeluar: TextView
    private lateinit var banyakStokMasuk: TextView
    private lateinit var navigateToProfileActivity: ImageView
    private var itemListStokKeluar = mutableListOf<StokKeluarData>()
    private var itemListStokMasuk = mutableListOf<StokMasukData>()


    fun getFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        email = view.findViewById(R.id.email)
        time = view.findViewById(R.id.time)
        banyakStokKeluar = view.findViewById(R.id.banyakStokKeluar)
        banyakStokMasuk = view.findViewById(R.id.banyakStokMasuk)
        navigateToProfileActivity = view.findViewById(R.id.navigateToProfileActivity)

        itemListStokKeluar = ArrayList()
        recyclerViewStokKeluar = view.findViewById(R.id.rvItemStokKeluar)
        recyclerViewStokKeluar.layoutManager = LinearLayoutManager(requireContext())
        stokKeluarAdapter = StokKeluarAdapter(this, itemListStokKeluar)

        itemListStokMasuk = ArrayList()
        recyclerViewStokMasuk = view.findViewById(R.id.rvItemStokMasuk)
        recyclerViewStokMasuk.layoutManager = LinearLayoutManager(requireContext())
        stokMasukAdapter = StokMasukAdapter(this, itemListStokMasuk)

        prepareItemListData()
        recyclerViewStokKeluar.adapter = stokKeluarAdapter
        recyclerViewStokMasuk.adapter = stokKeluarAdapter

        receivedEmail = requireArguments().getString("EMAIL").toString()
        time.text = "Recap, ${getFormattedDate()}"
        email.text = "Welcome Back, $receivedEmail!"
        banyakStokKeluar.text = itemListStokKeluar.size.toString()
        banyakStokMasuk.text = itemListStokMasuk.size.toString()

        navigateToProfileActivity.setOnClickListener(View.OnClickListener {
            val intentToProfileActivity = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intentToProfileActivity)
        })

        return view
    }

    private fun prepareItemListData() {
        // Laptop
        itemListStokKeluar.add(
            StokKeluarData(
                1,
                "Laptop",
                "ASUS Vivobook 15",
                "Laptop 15 inci dengan prosesor Intel Core i3",
                5000000.0
            )
        )
        itemListStokKeluar.add(
            StokKeluarData(
                5,
                "Laptop",
                "Acer Aspire 5",
                "Laptop 15 inci dengan RAM 8GB dan SSD 256GB",
                6000000.0
            )
        )
        itemListStokKeluar.add(
            StokKeluarData(
                7,
                "Laptop",
                "HP Pavilion 14",
                "Laptop 14 inci dengan desain tipis dan ringan",
                5500000.0
            )
        )
        itemListStokKeluar.add(
            StokKeluarData(
                9,
                "Laptop",
                "MacBook Air M1",
                "Laptop Apple dengan chip M1 yang powerful dan hemat daya",
                14000000.0
            )
        )
        itemListStokMasuk.add(
            StokMasukData(
                2,
                "Smartphone",
                "Samsung Galaxy A53 5G",
                "Smartphone 5G dengan kamera 64MP",
                6500000.0
            )
        )
        itemListStokMasuk.add(
            StokMasukData(
                4,
                "Smartphone",
                "Xiaomi Redmi Note 11 Pro",
                "Smartphone dengan layar AMOLED dan fast charging",
                4200000.0
            )
        )
        itemListStokMasuk.add(
            StokMasukData(
                6,
                "Smartphone",
                "Realme 9 Pro+",
                "Smartphone dengan kamera 50MP dan pengisian daya 60W",
                4500000.0
            )
        )
        itemListStokMasuk.add(
            StokMasukData(
                8,
                "Smartphone",
                "Vivo V23 5G",
                "Smartphone dengan kamera selfie 50MP dan desain ramping",
                6000000.0
            )
        )
        stokKeluarAdapter.notifyDataSetChanged()
        stokMasukAdapter.notifyDataSetChanged()
    }

}