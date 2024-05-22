package com.hisaki.stockwiseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator

class CameraActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_camera)

        val intentIntegrator = IntentIntegrator(this)
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        intentIntegrator.setPrompt("Scan a barcode or QR Code")
        intentIntegrator.setCameraId(0)
        intentIntegrator.setBarcodeImageEnabled(true)
        intentIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // Debug Purposes
                // Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                finish()
            } else {
                // Debug Purposes
                // Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                fetchData(result.contents)
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun fetchData(barcode: String) {
        db.collection("Product").whereEqualTo("barcode", barcode).get()
            .addOnCompleteListener { document ->
                val result = document.result.documents[0]
                val id = result.getString("id")
                val img = result.getString("img")
                val qrcode = result.getString("barcode")
                val name = result.getString("name")
                val price = result.getLong("price")
                val stock = result.getString("stock")

                val intentWithID = Intent(this, AdminStokBarang::class.java).apply {
                    putExtra("id", id)
                    putExtra("img", img)
                    putExtra("barcode", qrcode)
                    putExtra("name", name)
                    putExtra("price", price)
                    putExtra("stock", stock)
                }
                startActivity(intentWithID)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
    }
}