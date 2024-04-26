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

        replaceFragment(AdminHomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val itemId: Int = item.getItemId()
            if (itemId == R.id.home_button) {
                replaceFragment(AdminHomeFragment())
            } else if (itemId == R.id.stock_button) {
                replaceFragment(AdminStockFragment())
            }
            true
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(1).isEnabled = false
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}

