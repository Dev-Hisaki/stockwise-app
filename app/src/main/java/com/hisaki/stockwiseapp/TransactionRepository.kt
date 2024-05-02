import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.hisaki.stockwiseapp.Transaction
import java.util.*

class TransactionRepository {

    private val db = FirebaseFirestore.getInstance()

    fun addTransaction(transaction: Transaction) {
        // Dapatkan referensi ke koleksi transaksi
        val transactionRef = db.collection("Transaction")

        // Tambahkan transaksi ke Firestore
        transactionRef.add(transaction)
            .addOnSuccessListener { documentReference ->
                Log.d("TransactionRepository", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TransactionRepository", "Error adding document: $e")
            }
    }

    // Definisikan enum untuk tipe transaksi
    enum class TransactionType {
        IN,
        OUT
    }
}
