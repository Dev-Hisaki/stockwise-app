package com.hisaki.stockwiseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.hisaki.stockwiseapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: DB
    private val firebaseDB = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

            db = DB(this@RegisterActivity)
            btnRegister.setOnClickListener {
//                registerUser()
                processSignUp()
            }
        }
    }

    // Using SQLite
    private fun registerUser() {
        val username = binding.textInputUsername.text.toString()
        val email = binding.textInputEmail.text.toString()
        val password = binding.textInputPassword.text.toString()

        if (Validator.isTextNotEmpty(username) &&
            Validator.isTextNotEmpty(email) &&
            Validator.isTextNotEmpty(password)
        ) {
            if (Validator.isValidEmail(email)) {
                val user = User(username = username, email = email.trim(), password = password)
                db.registerUser(user)
                Toast.makeText(this, "User Registered !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please Input All Field", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processSignUp() {
        binding.apply {
            val username: String = textInputUsername.getText().toString()
            val email: String = textInputEmail.getText().toString()
            val password: String = textInputPassword.getText().toString()
            val name: String = username
            val role = "admin"

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
                            this@RegisterActivity,
                            "Pendaftaran berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Gagal melakukan pendaftaran",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "Mohon isi data dengan benar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}