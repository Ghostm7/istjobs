package com.example.istjobs.data.repository

import com.example.istjobs.data.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserProfileRepository {

    private val db = FirebaseFirestore.getInstance()

    // This method accepts a userId and returns the UserProfile
    suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val document = db.collection("userProfiles").document(userId).get().await()
            document.toObject(UserProfile::class.java) // Convert Firestore document to UserProfile
        } catch (e: Exception) {
            null // Handle exception if necessary
        }
    }
}
