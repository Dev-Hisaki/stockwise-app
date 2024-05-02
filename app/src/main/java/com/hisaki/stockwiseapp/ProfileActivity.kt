package com.hisaki.stockwiseapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hisaki.stockwiseapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var userEmail: String? = null
    private var userName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("shared_pref", MODE_PRIVATE)
        userEmail = sharedPreferences.getString("Email", null)
        userName = sharedPreferences.getString("Username", null)

        binding.apply {
            backToHome.setOnClickListener {
                finish()
            }
            logoutButton.setOnClickListener {
                val logoutIntent = Intent(this@ProfileActivity, LoginActivity::class.java)
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)   // Clearing all activity before going to LoginActivity
                startActivity(logoutIntent)
                destroySession()
            }
            testTransaction.setOnClickListener {
                val i = Intent(this@ProfileActivity, TransactionTest::class.java)
                startActivity(i)
            }
            inputLayoutUsername.text = userName
            inputLayoutEmail.text = userEmail
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