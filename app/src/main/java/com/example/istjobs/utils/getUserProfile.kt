package com.example.istjobs.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.istjobs.data.UserProfile

suspend fun getUserProfile(userId: String): UserProfile? {
    val db = FirebaseFirestore.getInstance()
    return try {
        val document = db.collection("userProfiles").document(userId).get().await() // Await Firestore result
        document.toObject(UserProfile::class.java) // Convert to UserProfile
    } catch (e: Exception) {
        Log.e("Firestore", "Error getting user profile", e) // Log error
        null // Return null on error
    }


}
