package com.hisaki.stockwiseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.hisaki.stockwiseapp.databinding.ActivityBuatAkunBinding

class BuatAkunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuatAkunBinding
    private val firebaseDB = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuatAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBuatAkun.setOnClickListener{
                processSignUp()
            }
        }
    }

    private fun processSignUp() {
        binding.apply {
            val username: String = textInputUsername.text.toString()
            val email: String = textInputEmail.text.toString()
            val password: String = textInputPassword.text.toString()
            val name: String = username
            val role = "user"

            val data: MutableMap<String, Any> = HashMap()
            data["email"] = email
            data["name"] = name
            data["username"] = username
            data["password"] = password
            data["role"] = role

            if (username.trim() != "" && email.trim() != "" && password.trim() != "") {
                firebaseDB.collection("User").document(email)
                    .set(data)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@BuatAkunActivity,
                            "User berhasil dibuat",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@BuatAkunActivity,
                            "User gagal dibuat",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    this@BuatAkunActivity,
                    "Mohon isi data dengan benar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}