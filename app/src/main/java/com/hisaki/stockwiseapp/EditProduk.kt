package com.hisaki.stockwiseapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class EditProduk : AppCompatActivity() {

    private lateinit var ivProductImage: ImageView
    private lateinit var etProductBarcode: EditText
    private lateinit var etProductName: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var etProductStock: EditText
    private lateinit var btnUpdateProduct: Button

    private lateinit var firestoreDB: FirebaseFirestore
    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_produk)

        ivProductImage = findViewById(R.id.ivItemImg)
        etProductBarcode = findViewById(R.id.tvItemBarcode)
        etProductName = findViewById(R.id.tvItemName)
        etProductPrice = findViewById(R.id.tvItemPrice)
        etProductStock = findViewById(R.id.tvItemStock)
        btnUpdateProduct = findViewById(R.id.updateStokButton)

        firestoreDB = FirebaseFirestore.getInstance()

        val backbtn: ImageView = findViewById(R.id.ivbackbtn)
        backbtn.setOnClickListener {
            onBackPressed()
        }

        val intent = intent
        productId = intent.getStringExtra("id")
        val img = intent.getStringExtra("img")
        val barcode = intent.getStringExtra("barcode")
        val name = intent.getStringExtra("name")
        val price = intent.getLongExtra("price", 0)
        val stock = intent.getStringExtra("stock")

        if (img != null && img.isNotEmpty()) {
            Glide.with(this)
                .load(img)
                .into(ivProductImage)
        } else {
            ivProductImage.setImageResource(R.drawable.img_profile_test)
        }

        etProductBarcode.setText(barcode)
        etProductName.setText(name)
        etProductPrice.setText(price.toString())
        etProductStock.setText(stock)

        btnUpdateProduct.setOnClickListener {
            updateProduct()
        }
    }

    private fun updateProduct() {
        val barcode = etProductBarcode.text.toString()
        val name = etProductName.text.toString()
        val price = etProductPrice.text.toString().toLongOrNull()
        val stock = etProductStock.text.toString()

        if (barcode.isEmpty() || name.isEmpty() || price == null || stock.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val productData = hashMapOf(
            "barcode" to barcode,
            "name" to name,
            "price" to price,
            "stock" to stock
        )

        productId?.let {
            firestoreDB.collection("Product").document(it)
                .update(productData as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update product: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun formatRupiah(number: Double): String {
        val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("in", "ID"))
        return format.format(number)
    }
}
