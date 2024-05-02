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
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
        binding.backToHome.setOnClickListener {
            finish()
        }
        binding.logoutButton.setOnClickListener {
            val logoutIntent = Intent(this, LoginActivity::class.java)
            startActivity(logoutIntent)
            destroySession()
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
>>>>>>> c3b6b8377304d3db3f61696e6e276c93a2559351
>>>>>>> Stashed changes
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