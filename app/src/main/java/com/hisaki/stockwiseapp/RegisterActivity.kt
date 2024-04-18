package com.hisaki.stockwiseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hisaki.stockwiseapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: DB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        db = DB(this)
        binding.btnRegister.setOnClickListener{
            registerUser()
        }
    }

    private fun registerUser(){
        val username = binding.textInputUsername.text.toString()
        val email = binding.textInputEmail.text.toString()
        val password = binding.textInputPassword.text.toString()

        if(Validator.isTextNotEmpty(username)&&
                Validator.isTextNotEmpty(email)&&
                Validator.isTextNotEmpty(password)
        ){
            if(Validator.isValidEmail(email)){
                val user = User(username = username, email = email.trim(), password = password)
                db.registerUser(user)
                Toast.makeText(this,"User Registered !", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Invalid Email Format", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Please Input All Field", Toast.LENGTH_SHORT).show()
        }
    }
}