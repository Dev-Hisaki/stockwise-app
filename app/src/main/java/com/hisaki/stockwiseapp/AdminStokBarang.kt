package com.hisaki.stockwiseapp

import TransactionRepository
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.OutputStream
import java.lang.Exception
import java.util.Date

class AdminStokBarang : AppCompatActivity() {
    private val transactionRepository = TransactionRepository()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var generateQRCodeButton: ImageView
    private var userName: String? = null
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("Product")
    private var roleUser: String? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_stok_barang)
        getSession()
        val id = intent.getStringExtra("id")
        val img = intent.getStringExtra("img")
        val barcode = intent.getStringExtra("barcode")
        val name = intent.getStringExtra("name")
        val price = intent.getLongExtra("price", 0)
        val stock = intent.getStringExtra("stock")
        val query = collectionRef.whereEqualTo("barcode", barcode)

        val ivItemImg: ImageView = findViewById(R.id.ivItemImg)
        val tvItemBarcode: TextView = findViewById(R.id.tvItemBarcode)
        val tvItemName: TextView = findViewById(R.id.tvItemName)
        val tvItemPrice: TextView = findViewById(R.id.tvItemPrice)
        val tvItemStock: TextView = findViewById(R.id.tvItemStock)

        Glide.with(this).load(img).into(ivItemImg)
        tvItemBarcode.text = barcode
        tvItemName.text = name
        tvItemPrice.text = formatRupiah(price.toDouble())
        tvItemStock.text = stock.toString()

        var stokSekarang = "0"

        val backbtn: ImageView = findViewById(R.id.ivbackbtn)
        val tambahStokButton: Button = findViewById(R.id.tambahStokButton)
        val kurangStokButton: Button = findViewById(R.id.kurangiStokButton)

        fun showBottomSheetDialog(type: String, name: String?, stock: String?) {
            sharedPreferences = this.getSharedPreferences("shared_pref", MODE_PRIVATE)
            userName = sharedPreferences.getString("Username", null)
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetView: View = if (type == "tambah") {
                layoutInflater.inflate(R.layout.activity_popup_tambah_stok_barang_admin, null)
            } else {
                layoutInflater.inflate(R.layout.activity_popup_kurangi_stok_barang_admin, null)
            }

            val namaBarang: TextView = bottomSheetView.findViewById(
                if (type == "tambah") R.id.namaBarangTambahStokTextView else R.id.namaBarangKurangiStokTextView
            )
            val stokBarang: TextView = bottomSheetView.findViewById(
                if (type == "tambah") R.id.stokBarangTambahStokTextView else R.id.stokBarangKurangiStokTextView
            )
            val ubahStokBarang: TextView = bottomSheetView.findViewById(
                if (type == "tambah") R.id.ubahStokTambahStokTextView else R.id.ubahStokKurangiStokTextView
            )
            val tambahButton: AppCompatButton = bottomSheetView.findViewById(
                if (type == "tambah") R.id.tambahAngkaTambahStokButton else R.id.tambahAngkaKurangiStokButton
            )
            val kurangButton: AppCompatButton = bottomSheetView.findViewById(
                if (type == "tambah") R.id.kurangAngkaTambahStokButton else R.id.kurangAngkaKurangiStokButton
            )
            val confirmButton: AppCompatButton = bottomSheetView.findViewById(
                if (type == "tambah") R.id.confirmTambahStokTextView else R.id.confirmKurangStokTextView
            )


            namaBarang.text = name
            stokBarang.text = stock
            ubahStokBarang.text = "0"

            tambahButton.setOnClickListener {
                val value = ubahStokBarang.text.toString().toLong() + 1
                ubahStokBarang.text = value.toString()
                stokSekarang = value.toString()
                Log.e("test", value.toString())
            }

            kurangButton.setOnClickListener {
                val value = ubahStokBarang.text.toString().toLong() - 1
                if (value >= 0) {
                    ubahStokBarang.text = value.toString()
                    stokSekarang = value.toString()
                }
            }

            confirmButton.setOnClickListener {
                if (ubahStokBarang.text == "0") {
                    Toast.makeText(
                        this@AdminStokBarang,
                        "Stok harus diisi",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    query.get().addOnSuccessListener { documents ->
                        for (document in documents) {
                            val documentId = document.id

                            val currentStock = stock!!.toInt()
                            val changeInStock = stokSekarang.toInt()
                            val newStock = if (type == "tambah") {
                                currentStock + changeInStock
                            } else {
                                currentStock - changeInStock
                            }

                            val updates = hashMapOf<String, Any>(
                                "stock" to newStock.toString()
                            )

                            collectionRef.document(documentId)
                                .update(updates)
                                .addOnSuccessListener {
                                    // Stok berhasil diperbarui di Firestore
                                    val newTransaction = Transaction(
                                        username = userName.toString(),
                                        productid = barcode.toString(),
                                        productname = name.toString(),
                                        productprice = price.toDouble(),
                                        productstock = newStock,
                                        quantity = changeInStock,
                                        type = if (type == "tambah") TransactionRepository.TransactionType.IN else TransactionRepository.TransactionType.OUT,
                                        date = Date()
                                    )

                                    tvItemStock.text = newStock.toString()

                                    transactionRepository.addTransaction(newTransaction)
                                    Toast.makeText(
                                        this@AdminStokBarang,
                                        if (type == "tambah") "Stok berhasil ditambahkan!" else "Stok berhasil dikurangi!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    bottomSheetDialog.dismiss()
                                }
                                .addOnFailureListener { e ->
                                    // Gagal memperbarui stok di Firestore
                                    Toast.makeText(
                                        this@AdminStokBarang,
                                        "Gagal memperbarui stok: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                }
            }


            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

        backbtn.setOnClickListener {
            onBackPressed()
        }

        if(roleUser == "admin"){
            tambahStokButton.setOnClickListener {
                showBottomSheetDialog("tambah", name, stock)
                val db = FirebaseFirestore.getInstance()
                db.collection("Product").whereEqualTo("barcode", barcode).get()
                    .addOnCompleteListener { document ->
                        val result = document.result.documents[0]
                        val imgFromFirebase = result.getString("img")
                        val qrcodeFromFirebase = result.getString("barcode")
                        val nameFromFirebase = result.getString("name")
                        val priceFromFirebase = result.getLong("price")
                        val stockFromFirebase = result.getString("stock")

                    }
            }
        } else {
            tambahStokButton.visibility = View.GONE
        }


        kurangStokButton.setOnClickListener {
            showBottomSheetDialog("kurang", name, stock)
        }

        generateQRCodeButton = findViewById(R.id.showQRCode)
        generateQRCodeButton.setOnClickListener {
            generateQRCode()
        }


    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun generateQRCode() {
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.popup_qrcode, null)
        builder.setView(dialogView)

        qrcodeGenerator(dialogView)

        val saveButton = dialogView.findViewById<AppCompatButton>(R.id.saveQRCodeButton)
        val qrCodeImage = dialogView.findViewById<ImageView>(R.id.barcodeImage)
        saveButton.setOnClickListener {
            if (qrCodeImage != null) {
                saveImage(qrCodeImage)
            }
        }

        val dialog = builder.create()

        dialog.show()
    }

    private fun qrcodeGenerator(view: View) {
        val qrcode = intent.getStringExtra("barcode").toString()
        var qrCodeImage = view.findViewById<ImageView>(R.id.barcodeImage)
        if (qrcode.isNotEmpty()) {
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap =
                    barcodeEncoder.encodeBitmap(qrcode, BarcodeFormat.QR_CODE, 500, 500)
                if (qrCodeImage != null) {
                    qrCodeImage.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(this, "QRCODEIMAGEVIEW NULL", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImage(qrCodeImage: ImageView) {
        val drawable = qrCodeImage.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/StockWise"
            )
        }

        val uri: Uri? =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            uri?.let {
                val outputStream: OutputStream? = contentResolver.openOutputStream(it)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                } else {
                    Toast.makeText(this, "OutputStream is null", Toast.LENGTH_SHORT).show()
                }
                outputStream?.close()
                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun getSession() {
        sharedPreferences = getSharedPreferences("shared_pref", AppCompatActivity.MODE_PRIVATE)
        roleUser = sharedPreferences.getString("Role", null)
    }
}