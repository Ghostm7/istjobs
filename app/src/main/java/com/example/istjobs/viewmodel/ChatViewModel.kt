import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages = _messages.asStateFlow()

    init {
        fetchMessages()
    }

    private fun fetchMessages() {
        // Replace "chatMessages" with your Firestore collection name
        db.collection("chatMessages")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val messagesList = snapshot.documents.map { it.getString("message") ?: "" }
                    _messages.value = messagesList
                }
            }
    }

    fun sendMessage(message: String) {
        // Replace "chatMessages" with your Firestore collection name
        db.collection("chatMessages").add(hashMapOf("message" to message))
    }
}
