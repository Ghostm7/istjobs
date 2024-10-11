package com.example.istjobs.utils

import android.content.Context
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

    var isFirstLogin = mutableStateOf(true) // Changed to a regular mutableState variable
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> get() = _authError


    fun checkAdminProfile(callback: (Boolean) -> Unit) {
        val adminId = FirebaseAuth.getInstance().currentUser?.uid
        if (adminId != null) {
            firestore.collection("adminProfiles").document(adminId).get()
                .addOnSuccessListener { document: com.google.firebase.firestore.DocumentSnapshot ->
                    callback(document.exists())
                }
                .addOnFailureListener { e: java.lang.Exception ->
                    // Handle error (e.g., show a message)
                    callback(false)
                }
        } else {
            callback(false)
        }
    }

    // Current Admin ID
    private val _currentAdminId = MutableStateFlow<String?>(null)
    val currentAdminId: StateFlow<String?> = _currentAdminId.asStateFlow()

    // Email validation function
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Helper function to validate signup fields
    private fun validateSignup(email: String, password: String, confirmPassword: String, context: Context): Boolean {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidEmail(email)) {
            Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    // Sign-up method for admins with profile creation
    fun adminSignup(email: String, password: String, confirmPassword: String, name: String, context: Context, onComplete: (Boolean) -> Unit) {
        if (!validateSignup(email, password, confirmPassword, context)) return

        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val adminId = authResult.user?.uid ?: return@launch

                // Create admin document in Firestore with "admin" role
                val adminData = hashMapOf(
                    "email" to email,
                    "role" to "admin",
                    "userID" to adminId
                )
                firestore.collection("users").document(adminId).set(adminData).await()

                // Save admin profile to "adminProfiles" collection
                val adminProfile = AdminProfile(
                    adminId = adminId,
                    name = name,
                    email = email
                )
                firestore.collection("adminProfiles").document(adminId).set(adminProfile).await()

                Toast.makeText(context, "Admin signup and profile creation successful", Toast.LENGTH_SHORT).show()
                onComplete(true)
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
                onComplete(false)
            }
        }
    }

    // Method to update admin profile
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

    // Sign-in method for role-based authentication
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

                // Check if the logged-in user matches the expected role
                val userDocument = firestore.collection("users").document(userId).get().await()
                val userRole = userDocument.getString("role") ?: ""

                if (userRole == role) {
                    _authError.value = null // Clear any previous error

                    // Store the current admin ID
                    _currentAdminId.value = userId // Store the admin ID after successful login

                    // Check if the admin profile exists
                    val adminProfileDocument = firestore.collection("adminProfiles").document(userId).get().await()
                    isFirstLogin.value = !adminProfileDocument.exists() // Set true if profile does not exist

                    onResult(true) // Successful login for the correct role
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

    fun adminLogin(email: String, password: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            signIn(email, password, "admin") { isSuccess ->
                onSuccess(isSuccess)
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

    // Method to get the current admin ID
    fun getCurrentAdminId(): String? {
        return _currentAdminId.value
    }

    suspend fun saveAdminProfile(adminId: String, name: String, address: String, context: Context) {
        try {
            val adminProfile = AdminProfile(
                adminId = adminId,
                name = name,
                email = FirebaseAuth.getInstance().currentUser?.email ?: "",
                address = address
            )
            firestore.collection("adminProfiles").document(adminId).set(adminProfile).await()
            Toast.makeText(context, "Admin profile saved successfully", Toast.LENGTH_SHORT).show()
            isFirstLogin.value = false // Set isFirstLogin to false after profile creation
        } catch (e: Exception) {
            Toast.makeText(context, "Error saving admin profile: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
