package com.hisaki.stockwiseapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.hisaki.stockwiseapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userEmail: String
    private lateinit var userName: String
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("shared_pref", MODE_PRIVATE)
        userEmail = sharedPreferences.getString("Email", null) ?: ""
        userName = sharedPreferences.getString("Username", null) ?: ""

        getProfileImage()

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
            buatAkunButton.setOnClickListener {
                val i = Intent(this@ProfileActivity, BuatAkunActivity::class.java)
                startActivity(i)
            }
            kelolaAkunButton.setOnClickListener {
                val kelolaAkunIntent = Intent(this@ProfileActivity, KelolaUserActivity::class.java)
                startActivity(kelolaAkunIntent)
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

    private fun getProfileImage() {
        db.collection("User").document(userEmail).get()
            .addOnCompleteListener { document ->
                val result = document.result
                val imgurl: String = result.getString("img") ?: ""
                Glide.with(this)
                    .load(imgurl)
                    .into(binding.imageProfile)
            }
    }
}
