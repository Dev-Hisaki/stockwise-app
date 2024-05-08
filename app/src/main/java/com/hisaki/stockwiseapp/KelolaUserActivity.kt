package com.hisaki.stockwiseapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.hisaki.stockwiseapp.databinding.ActivityKelolaUserBinding

class KelolaUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKelolaUserBinding
    private lateinit var recycleViewAdmin: RecyclerView
    private lateinit var recycleViewUser: RecyclerView
    private lateinit var adminAdapter: AdminAdapter
    private lateinit var userAdapter: UserAdapter
    private var itemListAdmin = mutableListOf<AdminData>()
    private var itemListUser = mutableListOf<UserData>()
    private lateinit var banyakAdmin: TextView
    private lateinit var banyakUser: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKelolaUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banyakAdmin = findViewById(R.id.banyakAdmin)
        banyakUser = findViewById(R.id.banyakUser)

        val adminRef = FirebaseFirestore.getInstance().collection("User").whereEqualTo("role", "admin")
        adminRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val adminGetData = document.data
                val name = adminGetData["name"] as String
                val email = adminGetData["email"] as String
                val role = adminGetData["role"] as String

                val adminData = AdminData(
                    name = name,
                    email = email,
                    role = role
                )
                itemListAdmin.add(adminData)
            }
            banyakAdmin.text = itemListAdmin.size.toString()
            adminAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.e("KelolaUserActivity", "Error getting admin in data", exception)
        }

        val userRef = FirebaseFirestore.getInstance().collection("User").whereEqualTo("role", "user")
        userRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val userGetData = document.data
                val name = userGetData["name"] as String
                val email = userGetData["email"] as String
                val role = userGetData["role"] as String

                val userData = UserData(
                    name = name,
                    email = email,
                    role = role
                )
                itemListUser.add(userData)
            }
            banyakUser.text = itemListUser.size.toString()
            userAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.e("KelolaUserActivity", "Error getting user in data", exception)
        }

        binding.apply {
            backToProfileImageView.setOnClickListener {
                finish()
            }

            adminAdapter = AdminAdapter(itemListAdmin)
            recycleViewAdmin = rvItemAdmin
            recycleViewAdmin.layoutManager = LinearLayoutManager(this@KelolaUserActivity)
            recycleViewAdmin.adapter = adminAdapter

            userAdapter = UserAdapter(itemListUser)
            recycleViewUser = rvItemUSer
            recycleViewUser.layoutManager = LinearLayoutManager(this@KelolaUserActivity)
            recycleViewUser.adapter = userAdapter
        }
        /*enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linearLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }
}