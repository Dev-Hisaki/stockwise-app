package com.hisaki.stockwiseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminStockFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerItemStockAdapter: RecyclerItemStockAdapter
    private var itemList = mutableListOf<ItemStock>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_stock, container, false)

        // Fragment Stock
        itemList = ArrayList()

        recyclerView = view.findViewById(R.id.rvItemStock)
        recyclerItemStockAdapter = RecyclerItemStockAdapter(this, itemList)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerItemStockAdapter

        prepareItemListData()

        return view
    }

    private fun prepareItemListData() {
        itemList.add(ItemStock("Televisi", R.drawable.televisi_dummy))
        itemList.add(ItemStock("Kulkas", R.drawable.kulkas_dummy))
        recyclerItemStockAdapter.notifyDataSetChanged() // Notify adapter after adding data
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminStockFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
