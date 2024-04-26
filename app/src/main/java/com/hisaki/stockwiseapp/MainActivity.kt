package com.hisaki.stockwiseapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hisaki.stockwiseapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedEmail = intent.getStringExtra("EMAIL").toString()

        replaceFragment(AdminHomeFragment(), receivedEmail)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val itemId: Int = item.getItemId()
            if (itemId == R.id.home_button) {
                replaceFragment(AdminHomeFragment(), receivedEmail)
            } else if (itemId == R.id.stock_button) {
                replaceFragment(AdminStockFragment()) // Replace with your Stock Fragment
            }
            true
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false
    }

    private fun replaceFragment(fragment: Fragment, email: String = "") {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (email.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putString("EMAIL", email)
            fragment.arguments = bundle
        }
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}
