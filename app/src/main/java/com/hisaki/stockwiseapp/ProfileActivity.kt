package com.hisaki.stockwiseapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hisaki.stockwiseapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backToHome.setOnClickListener {
            finish()
        }
        binding.logoutButton.setOnClickListener {
            val logoutIntent = Intent(this, LoginActivity::class.java)
            startActivity(logoutIntent)
            destroySession()
        }
    }

    // Destroying session for logout
    private fun destroySession() {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences("shared_pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        this.finish()
    }
}