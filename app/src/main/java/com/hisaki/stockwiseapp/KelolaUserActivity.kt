package com.hisaki.stockwiseapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.hisaki.stockwiseapp.databinding.ActivityKelolaUserBinding
import org.w3c.dom.Text

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
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection("User")
    private lateinit var dialogAwal: AlertDialog

    private fun detailUserDialog(userName: String, userEmail: String) {
        val dialogView = layoutInflater.inflate(R.layout.popup_detail_kelola_akun_user, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val dialog = dialogBuilder.create()
        dialogAwal = dialog
        val btnDeleteUser = dialogView.findViewById<TextView>(R.id.deleteUserButton)
        val btnChangeAccess = dialogView.findViewById<TextView>(R.id.accessButton)

        dialog.setOnShowListener {
            val popupUserName = dialogView.findViewById<TextView>(R.id.userNameTextView)
            popupUserName?.text = "User $userName"
        }
        btnDeleteUser.setOnClickListener {
            deleteUserDialog(userName, userEmail)
        }
        btnChangeAccess.setOnClickListener {
            changeAccessDialog(userName, userEmail)
        }
        dialog.show()
    }

    private fun changeAccessDialog(userName: String, userEmail: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.popup_ubah_hak_akses, null)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()

        val accessTextView = dialogView.findViewById<TextView>(R.id.accessTextView)
        val changeAdminButton = dialogView.findViewById<Button>(R.id.changeAdminButton)
        val cancelAccessButton = dialogView.findViewById<Button>(R.id.cancelAccessButton)

        accessTextView.text =
            "Apakah anda yakin ingin mengubah hak akses user $userName menjadi admin?"

        changeAdminButton.setOnClickListener {
            val query = collectionRef.whereEqualTo("email", userEmail)

            query.get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentId = document.id

                    val updates = hashMapOf<String, Any>(
                        "role" to "admin"
                    )

                    collectionRef.document(documentId)
                        .update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "User berhasil menjadi admin",
                                Toast.LENGTH_SHORT
                            ).show()
                            val selectedUser = itemListUser.find { it.email == userEmail }

                            selectedUser?.let {
                                itemListUser.remove(it)
                                userAdapter.notifyDataSetChanged()
                            }

                            dialog.dismiss()
                            dialogAwal.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "User gagal menjadi admin",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }.addOnFailureListener { e ->
                Log.e("UpdateUserRole", "Gagal mendapatkan user", e)
            }
        }
        cancelAccessButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteUserDialog(userName: String, userEmail: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.popup_hapus_akun, null)
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        val deleteUserTextView = dialogView.findViewById<TextView>(R.id.deleteUserTextView)
        val confirmDeleteUserButton = dialogView.findViewById<Button>(R.id.confirmDeleteUserButton)
        val cancelDeleteButton = dialogView.findViewById<Button>(R.id.cancelDeleteButton)
        deleteUserTextView.text = "Apakah anda yakin ingin menghapus akun $userName?"
        confirmDeleteUserButton.setOnClickListener {
            val query = collectionRef.whereEqualTo("email", userEmail)
            query.get().addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete().addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "User berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()

                        val userToRemove = itemListUser.find { it.email == userEmail }
                        userToRemove?.let {
                            itemListUser.remove(it)
                            userAdapter.notifyDataSetChanged()
                        }
                        dialog.dismiss()
                        dialogAwal.dismiss()
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "User gagal dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.addOnFailureListener { e ->
                Log.e("DeleteUser", "Gagal mendapatkan user", e)
            }
        }
        cancelDeleteButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKelolaUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banyakAdmin = findViewById(R.id.banyakAdmin)
        banyakUser = findViewById(R.id.banyakUser)

        val adminRef =
            FirebaseFirestore.getInstance().collection("User").whereEqualTo("role", "admin")
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

        val userRef =
            FirebaseFirestore.getInstance().collection("User").whereEqualTo("role", "user")
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

            userAdapter.setOnItemClickListener { userData ->
                detailUserDialog(
                    userData.name,
                    userData.email
                )
            }
        }
    }
}