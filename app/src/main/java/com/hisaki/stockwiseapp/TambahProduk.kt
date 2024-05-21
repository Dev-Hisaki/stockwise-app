package com.hisaki.stockwiseapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hisaki.stockwiseapp.databinding.ActivityTambahProdukBinding

class TambahProduk : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: ActivityTambahProdukBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Enable Edge-to-Edge
        enableEdgeToEdge()

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set OnClickListener for image upload button
        binding.imgTambahproduk.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1001)
        }

        // Set OnClickListener for add product button
        binding.btnTambahproduk.setOnClickListener {
            uploadImageAndSaveProduct()
            finish()
        }

        binding.btnback.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            binding.imgTambahproduk.setImageURI(imageUri)
        }
    }

    private fun uploadImageAndSaveProduct() {
        val barcode = binding.edbarcode.text.toString().trim()
        val name = binding.ednama.text.toString().trim()
        val priceStr = binding.edharga.text.toString().trim()
        val stock = binding.edstock.text.toString().trim()

        if (barcode.isEmpty() || name.isEmpty() || priceStr.isEmpty() || stock.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val price: Long
        try {
            price = priceStr.toLong()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Harga harus berupa angka", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null) {
            Toast.makeText(this, "Silakan pilih gambar", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef: StorageReference = storage.reference.child("images/${System.currentTimeMillis()}.jpg")
        val uploadTask = storageRef.putFile(imageUri!!)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                getNextProductId { nextId ->
                    val itemStock = ItemStock(
                        id = nextId, barcode = barcode, name = name, price = price, stock = stock, img = imageUrl
                    )

                    // Tambahkan produk ke Firestore
                    db.collection("Product").document(nextId.toString()).set(itemStock).addOnSuccessListener {
                        Toast.makeText(this, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Gagal menambahkan produk", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getNextProductId(callback: (String) -> Unit) {
        val counterRef = db.collection("Counters").document("ProductID")
        db.runTransaction { transaction ->
            val snapshot = transaction.get(counterRef)
            val currentId = snapshot.getLong("lastID") ?: 0
            val nextId = (currentId + 1).toString()
            transaction.update(counterRef, "lastID", currentId + 1)
            nextId
        }.addOnSuccessListener { nextId ->
            callback(nextId)
        }.addOnFailureListener { e ->
            Log.e("Tambah produk", "gagal mendapatkan id produk", e)
            Toast.makeText(this@TambahProduk, "Gagal mendapatkan ID produk", Toast.LENGTH_SHORT).show()
        }
    }
}
