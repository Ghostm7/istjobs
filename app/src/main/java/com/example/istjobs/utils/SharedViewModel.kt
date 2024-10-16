package com.example.istjobs.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
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
import com.example.istjobs.data.AdminProfile // Import your AdminProfile model here
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = Firebase.firestore

    var isFirstLogin = mutableStateOf(true) // Indicates if this is the first login
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> get() = _authError

    // Current Admin ID
    private val _currentAdminId = MutableStateFlow<String?>(null)
    val currentAdminId: StateFlow<String?> = _currentAdminId.asStateFlow()

    // Example of an authError StateFlow
    private val db = FirebaseFirestore.getInstance()

    // Email validation function
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Helper function to display Toast messages
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Check if the admin profile exists in Firestore
    fun checkAdminProfile(callback: (Boolean) -> Unit) {
        val adminId = firebaseAuth.currentUser?.uid
        if (adminId != null) {
            firestore.collection("adminProfiles").document(adminId).get()
                .addOnSuccessListener { document ->
                    callback(document.exists())
                }
                .addOnFailureListener {
                    callback(false)
                }
        } else {
            callback(false)
        }
    }

    // Check if a user profile exists in Firestore
    fun checkUserProfile(onResult: (Boolean) -> Unit) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            onResult(false) // No user is logged in
            return
        }

        db.collection("userProfiles").document(userId).get()
            .addOnSuccessListener { document ->
                onResult(document.exists()) // Return true if the document exists
            }
            .addOnFailureListener { exception ->
                Log.e("SharedViewModel", "Error checking user profile", exception)
                onResult(false) // Return false on failure
            }
    }

    // Validate signup fields
    private fun validateSignup(email: String, password: String, confirmPassword: String, context: Context): Boolean {
        if (email.isBlank() || password.isBlank()) {
            showToast(context, "Email and Password cannot be empty")
            return false
        }

        if (!isValidEmail(email)) {
            showToast(context, "Invalid email format")
            return false
        }

        if (password != confirmPassword) {
            showToast(context, "Passwords do not match!")
            return false
        }

        return true
    }

    // Admin signup with profile creation
    fun adminSignup(email: String, password: String, confirmPassword: String, name: String, context: Context, onComplete: (Boolean) -> Unit) {
        if (!validateSignup(email, password, confirmPassword, context)) return

        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val adminId = authResult.user?.uid ?: return@launch

                // Create admin document in Firestore
                val adminData = hashMapOf(
                    "email" to email,
                    "role" to "admin",
                    "userID" to adminId
                )
                firestore.collection("users").document(adminId).set(adminData).await()

                // Save admin profile
                val adminProfile = AdminProfile(
                    adminId = adminId,
                    name = name,
                    email = email
                )
                firestore.collection("adminProfiles").document(adminId).set(adminProfile).await()

                showToast(context, "Admin signup and profile creation successful")
                onComplete(true)
            } catch (e: Exception) {
                showToast(context, e.message ?: "Signup failed")
                onComplete(false)
            }
        }
    }

    // Update admin profile method
    fun updateAdminProfile(adminId: String, updatedProfile: AdminProfile, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                firestore.collection("adminProfiles").document(adminId).set(updatedProfile).await()
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    // Sign-in method with role-based authentication
    suspend fun signIn(email: String, password: String, role: String, onResult: (Boolean) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _authError.value = "Email and Password cannot be empty"
            onResult(false)
            return
        }

        if (!isValidEmail(email)) {
            _authError.value = "Invalid email format"
            onResult(false)
            return
        }

        viewModelScope.launch {
            try {
                val userAuthResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val userId = userAuthResult.user?.uid ?: return@launch

                // Check user role
                val userDocument = firestore.collection("users").document(userId).get().await()
                val userRole = userDocument.getString("role") ?: ""

                if (userRole == role) {
                    _authError.value = null // Clear any previous error
                    _currentAdminId.value = userId // Store the admin ID

                    // Check if the admin profile exists
                    val adminProfileDocument = firestore.collection("adminProfiles").document(userId).get().await()
                    isFirstLogin.value = !adminProfileDocument.exists() // Update first login status

                    onResult(true) // Successful login
                } else {
                    _authError.value = "Unauthorized login attempt for $role role"
                    firebaseAuth.signOut() // Log out if the role doesn't match
                    onResult(false)
                }
            } catch (e: Exception) {
                _authError.value = e.message // Set error message
                onResult(false)
            }
        }
    }

    // User signup method
    fun userSignup(email: String, password: String, confirmPassword: String, context: Context) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                showToast(context, "Email and Password cannot be empty")
                return@launch
            }

            if (!isValidEmail(email)) {
                showToast(context, "Invalid email format")
                return@launch
            }

            if (password != confirmPassword) {
                showToast(context, "Passwords do not match!")
                return@launch
            }

            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: return@launch

                // Create user document
                val userData = hashMapOf(
                    "email" to email,
                    "role" to "user",
                    "userID" to userId
                )
                firestore.collection("users").document(userId).set(userData).await()
                showToast(context, "User signup successful")
            } catch (e: Exception) {
                showToast(context, e.message ?: "Signup failed")
            }
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
                showToast(context, "Data retrieved successfully")
            }
        } catch (exception: Exception) {
            showToast(context, "Error retrieving data: ${exception.message}")
            null
        }
    }

    // Save user data
    suspend fun saveData(userData: UserData, context: Context) {
        try {
            firestore.collection("users").document(userData.userId).set(userData).await()
            showToast(context, "Data saved successfully")
        } catch (exception: Exception) {
            showToast(context, "Error saving data: ${exception.message}")
        }
    }

    // Delete user data
    suspend fun deleteData(userID: String, context: Context) {
        try {
            firestore.collection("users").document(userID).delete().await()
            showToast(context, "Data deleted successfully")
        } catch (exception: Exception) {
            showToast(context, "Error deleting data: ${exception.message}")
        }
    }
}