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
import com.example.istjobs.models.UserData


class SharedViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = Firebase.firestore

    // Define a MutableStateFlow for authentication error handling
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> get() = _authError

    // Email validation function
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Sign-in method for role-based authentication
    fun signIn(email: String, password: String, role: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _authError.value = "Email and Password cannot be empty"
                onSuccess(false)
                return@launch
            }

            if (!isValidEmail(email)) {
                _authError.value = "Invalid email format"
                onSuccess(false)
                return@launch
            }

            try {
                val userAuthResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val userId = userAuthResult.user?.uid ?: return@launch

                // Check if the logged-in user matches the expected role
                val userDocument = firestore.collection("users").document(userId).get().await()
                val userRole = userDocument.getString("role") ?: ""

                if (userRole == role) {
                    _authError.value = null // Clear any previous error
                    onSuccess(true) // Successful login for the correct role
                } else {
                    _authError.value = "Unauthorized login attempt for $role role"
                    firebaseAuth.signOut() // Log out if the role doesn't match
                    onSuccess(false)
                }
            } catch (e: Exception) {
                _authError.value = e.message // Set error message
                onSuccess(false)
            }
        }
    }

    // Sign-up method for users (Role: "user")
    fun userSignup(email: String, password: String, confirmPassword: String, context: Context) {
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
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: return@launch

                // Create user document with "user" role
                val userData = hashMapOf(
                    "email" to email,
                    "role" to "user",
                    "userID" to userId
                )
                firestore.collection("users").document(userId).set(userData).await()
                Toast.makeText(context, "User signup successful", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Sign-up method for admins (Role: "admin")
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
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val adminId = authResult.user?.uid ?: return@launch

                // Create admin document with "admin" role
                val adminData = hashMapOf(
                    "email" to email,
                    "role" to "admin",
                    "userID" to adminId
                )
                firestore.collection("users").document(adminId).set(adminData).await()
                Toast.makeText(context, "Admin signup successful", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Admin login method
    fun adminLogin(email: String, password: String, onSuccess: (Boolean) -> Unit) {
        signIn(email, password, "admin") { isSuccess ->
            onSuccess(isSuccess)
        }
    }

    // Save user profile method
    fun saveUserProfile(userProfile: com.example.istjobs.screen.UserProfile, onComplete: (Boolean) -> Unit) {
        firestore.collection("userProfiles").document(userProfile.userId)
            .set(userProfile)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
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
            firestore.collection("users").document(userData.userId).set(userData).await()
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
