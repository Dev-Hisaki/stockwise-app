package com.hisaki.stockwiseapp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hisaki.stockwiseapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userEmail: String
    private lateinit var userName: String
    private lateinit var userRole: String
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val fs: FirebaseStorage = FirebaseStorage.getInstance()
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("shared_pref", MODE_PRIVATE)
        userEmail = sharedPreferences.getString("Email", null) ?: ""
        userName = sharedPreferences.getString("Username", null) ?: ""
        userRole = sharedPreferences.getString("Role", null) ?: ""

        getProfileImage()

        binding.apply {
            backToHome.setOnClickListener {
                finish()
            }
            logoutButton.setOnClickListener {
                val logoutIntent = Intent(this@ProfileActivity, LoginActivity::class.java)
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)   // Clearing all activity before going to LoginActivity
                startActivity(logoutIntent)
                destroySession()
            }
            buatAkunButton.setOnClickListener {
                val i = Intent(this@ProfileActivity, BuatAkunActivity::class.java)
                startActivity(i)
            }
            if(userRole == "admin"){
                kelolaAkunButton.setOnClickListener {
                    val kelolaAkunIntent = Intent(this@ProfileActivity, KelolaUserActivity::class.java)
                    startActivity(kelolaAkunIntent)
                }
            } else {
                kelolaAkunButton.visibility = View.GONE
            }

            imageProfile.setOnClickListener {
                chooseImage()
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
                    .placeholder(R.drawable.img_loading)
                    .into(binding.imageProfile)
            }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            binding.imageProfile.setImageURI(imageUri)
            imageUri?.let { uploadImage(it) }
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val storageRef: StorageReference =
            fs.reference.child("ProfileImage/pfp-${System.currentTimeMillis()}.jpg")
        val uploadTask = storageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                db.collection("User").document(userEmail).update("img", imageUrl)
                    .addOnSuccessListener {
                        getProfileImage()
                        Toast.makeText(
                            this,
                            "Gambar berhasil diunggah dan URL diperbarui",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Gagal memperbarui URL gambar di Firestore",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Gagal mendapatkan URL gambar dari Firebase Storage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show()
        }
    }
}
