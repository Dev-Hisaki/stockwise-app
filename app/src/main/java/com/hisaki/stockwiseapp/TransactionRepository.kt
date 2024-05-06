import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.hisaki.stockwiseapp.Transaction
import java.util.*

class TransactionRepository {

    private val db = FirebaseFirestore.getInstance()
    private val transactionCollection = db.collection("Transaction")
    private val lastTransactionIdRef = db.collection("Meta").document("lastTransactionId")

    fun addTransaction(transaction: Transaction) {
        lastTransactionIdRef.get()
            .addOnSuccessListener { document ->
                val lastId = document.getLong("lastId") ?: 0
                val newId = lastId + 1

                transaction.id = newId.toInt()

                transactionCollection.document(newId.toString())
                    .set(transaction)
                    .addOnSuccessListener {
                        Log.d("TransactionRepository", "Transaction added with ID: $newId")
                        lastTransactionIdRef.update("lastId", newId)
                    }
                    .addOnFailureListener { e ->
                        Log.w("TransactionRepository", "Error adding transaction", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("TransactionRepository", "Error getting last transaction ID", e)
            }
    }
    enum class TransactionType {
        IN,
        OUT
    }
}

