package com.hisaki.stockwiseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/*private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"*/

/**
 * A simple [Fragment] subclass.
 * Use the [AdminHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminHomeFragment : Fragment() {
    private lateinit var time: TextView
    private lateinit var email: TextView
    private lateinit var receivedEmail: String

    /*private lateinit var recyclerView: RecyclerView
    private lateinit var stokMasukAdapter: StokMasukAdapter
    private var itemList = mutableListOf<StokMasukData>()*/

    /*// TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null*/

    fun getFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)

        // Initialize views
        time = view.findViewById(R.id.time)
        email = view.findViewById(R.id.email)
        receivedEmail = requireArguments().getString("EMAIL").toString()
        /*itemList = ArrayList()
        recyclerView = view.findViewById(R.id.rvItemStokMasuk)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        stokMasukAdapter = StokMasukAdapter(this, itemList)
        recyclerView.adapter = stokMasukAdapter*/

        // Set text for time and email
        time.text = "Recap, ${getFormattedDate()}"
        email.text = "Welcome Back, $receivedEmail!"

        return view
    }

    /*private fun prepareItemListData() {
        // Laptop
        itemList.add(StokMasukData(1, "Laptop", "ASUS Vivobook 15", "Laptop 15 inci dengan prosesor Intel Core i3", 5000000.0))
        itemList.add(StokMasukData(5, "Laptop", "Acer Aspire 5", "Laptop 15 inci dengan RAM 8GB dan SSD 256GB", 6000000.0))
        itemList.add(StokMasukData(7, "Laptop", "HP Pavilion 14", "Laptop 14 inci dengan desain tipis dan ringan", 5500000.0))
        itemList.add(StokMasukData(9, "Laptop", "MacBook Air M1", "Laptop Apple dengan chip M1 yang powerful dan hemat daya", 14000000.0))
        itemList.add(StokMasukData(11, "Laptop", "ASUS ROG Zephyrus G14", "Laptop gaming dengan desain tipis dan performa tinggi", 20000000.0))
        itemList.add(StokMasukData(13, "Laptop", "Lenovo Legion 5", "Laptop gaming dengan layar 144Hz dan performa RTX 3050", 13000000.0))
        itemList.add(StokMasukData(2, "Smartphone", "Samsung Galaxy A53 5G", "Smartphone 5G dengan kamera 64MP", 6500000.0))
        itemList.add(StokMasukData(4, "Smartphone", "Xiaomi Redmi Note 11 Pro", "Smartphone dengan layar AMOLED dan fast charging", 4200000.0))
        itemList.add(StokMasukData(6, "Smartphone", "Realme 9 Pro+", "Smartphone dengan kamera 50MP dan pengisian daya 60W", 4500000.0))
        itemList.add(StokMasukData(8, "Smartphone", "Vivo V23 5G", "Smartphone dengan kamera selfie 50MP dan desain ramping", 6000000.0))
        itemList.add(StokMasukData(10, "Smartphone", "iPhone 13", "Smartphone Apple dengan kamera terbaik dan performa tangguh", 12000000.0))
        itemList.add(StokMasukData(12, "Smartphone", "Samsung Galaxy S22 Ultra", "Smartphone flagship Samsung dengan kamera zoom 100x", 17000000.0))
        stokMasukAdapter.notifyDataSetChanged() // Notify adapter after adding data
    }*/


    /*companion object {
        *//**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminHomeFragment.*//*

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}