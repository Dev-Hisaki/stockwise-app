package com.hisaki.stockwiseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hisaki.stockwiseapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DB(this)

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener{
            loginUser()
        }
    }
    private fun loginUser(){
        val email = binding.textInputEmail.text.toString().trim()
        val password = binding.textInputPassword.text.toString().trim()

        if(Validator.isTextNotEmpty(email)&& Validator.isTextNotEmpty(password)){
            if(Validator.isTextNotEmpty(email)){
                val isSuccess = db.loginUser(email, password)
                if(isSuccess){
                    val i = Intent(this,MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }else{
                Toast.makeText(this,"Invalid Email Format", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Please enter all field", Toast.LENGTH_SHORT).show()
        }
    }
}
