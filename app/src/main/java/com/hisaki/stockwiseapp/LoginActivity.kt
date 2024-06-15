package com.hisaki.stockwiseapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hisaki.stockwiseapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: DB
    private val firebaseDB = FirebaseFirestore.getInstance()

    // For assigning email and password to session
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var emailUser: String? = null
    private var passwordUser: String? = null
    private var roleUser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DB(this)

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    override fun onStart() {
        super.onStart()
        getSession()
        if (emailUser != null && passwordUser != null) {
            val intentDirectly = Intent(this, MainActivity::class.java)
            startActivity(intentDirectly)
            finish()
        }
    }

    private fun loginUser() {
        binding.apply {
            val email = textInputEmail.text.toString().trim()
            val password = textInputPassword.text.toString().trim()
            if (Validator.isTextNotEmpty(email) && Validator.isTextNotEmpty(password)) {
                if (Validator.isTextNotEmpty(email)) {
                    firebaseDB.collection("User").document(email).get()
                        .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val emailFromFirebase = documentSnapshot.getString("email")
                                val passwordFromFirebase = documentSnapshot.getString("password")
                                val roleFromFirebase = documentSnapshot.getString("role")
                                if (email == emailFromFirebase && password == passwordFromFirebase) {
                                    // Memindahkan pengaturan session ke sini
                                    val username = documentSnapshot.getString("username")
                                    setSession(
                                        email,
                                        password,
                                        username,
                                        roleFromFirebase
                                    )
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Email atau Password Salah",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "User tidak ditemukan",
                                    Toast.LENGTH_SHORT
                                ).show()
                                clearInput()
                            }
                        }
                        .addOnFailureListener { e: Exception? ->
                            Toast.makeText(
                                this@LoginActivity,
                                "Login Gagal : " + e?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            clearInput()
                        }
                }
            } else {
                Toast.makeText(this@LoginActivity, "Please enter all field", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun setSession(email: String?, password: String?, username: String?, role: String?) {
        sharedPreferences = getSharedPreferences("shared_pref", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString("Email", email)
        editor.putString("Password", password)
        editor.putString("Username", username)
        editor.putString("Role", role)
        editor.apply()
    }


    private fun getSession() {
        sharedPreferences = getSharedPreferences("shared_pref", MODE_PRIVATE)
        emailUser = sharedPreferences.getString("Email", null)
        passwordUser = sharedPreferences.getString("Password", null)
        roleUser = sharedPreferences.getString("Role", null)
    }

    private fun clearInput() {
        binding.apply {
            textInputEmail.setText("")
            textInputPassword.setText("")
        }
    }
}