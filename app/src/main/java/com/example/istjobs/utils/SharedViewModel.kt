package com.example.istjobs.utils

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SharedViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = Firebase.firestore

    // Define a MutableStateFlow for auth error handling
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> get() = _authError
// Inside SharedViewModel

    // Email validation function
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Sign-in method for authentication
    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _authError.value = "Email and Password cannot be empty"
                return@launch
            }

            if (!isValidEmail(email)) {
                _authError.value = "Invalid email format"
                return@launch
            }

            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                _authError.value = null // Clear any previous error
                onSuccess() // Navigate to dashboard or perform other actions on success
            } catch (e: Exception) {
                _authError.value = e.message // Set error message
            }
        }
    }

    // Admin signup method
    fun adminSignup(email: String, password: String, confirmPassword: String, context: Context) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (!isValidEmail(email)) {
                Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@launch
            }

            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Admin login method
    fun adminLogin(email: String, password: String, context: Context) {
        viewModelScope.launch {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Retrieve user data by userID
    suspend fun retrieveData(userID: String, context: Context): UserData? {
        return try {
            val document = firestore.collection("users").document(userID).get().await()
            document.toObject(UserData::class.java)?.also {
                Toast.makeText(context, "Data retrieved successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (exception: Exception) {
            Toast.makeText(context, "Error retrieving data: ${exception.message}", Toast.LENGTH_SHORT).show()
            null
        }
    }

    // Save user data
    suspend fun saveData(userData: UserData, context: Context) {
        try {
            firestore.collection("users").document(userData.userID).set(userData).await()
            Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show()
        } catch (exception: Exception) {
            Toast.makeText(context, "Error saving data: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Delete user data
    suspend fun deleteData(userID: String, context: Context) {
        try {
            firestore.collection("users").document(userID).delete().await()
            Toast.makeText(context, "Data deleted successfully", Toast.LENGTH_SHORT).show()
        } catch (exception: Exception) {
            Toast.makeText(context, "Error deleting data: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
