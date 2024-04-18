package com.hisaki.stockwiseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hisaki.stockwiseapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var binding: ActivityMainBinding
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}