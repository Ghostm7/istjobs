package com.example.istjobs.utils

import android.util.Log
import com.example.istjobs.data.Application
import com.google.firebase.firestore.FirebaseFirestore

class JobRepository {
    private val db = FirebaseFirestore.getInstance()

    // Function to fetch applications for a specific user
    fun fetchApplicationsForUser(userId: String, onSuccess: (List<Application>) -> Unit, onFailure: (String) -> Unit) {
        db.collection("users")
            .document(userId)
            .collection("jobApplications")
            .get()
            .addOnSuccessListener { documents ->
                val applications = documents.mapNotNull { it.toObject(Application::class.java) }
                onSuccess(applications)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error fetching applications for user $userId: ${e.message}", e)
                onFailure(e.message ?: "Unknown error")
            }
    }

    // Function to add an application to Firestore
    fun addApplication(application: Application, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        db.collection("applications")
            .add(application)
            .addOnSuccessListener { documentReference ->
                onSuccess(documentReference.id)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding application: ${e.message}", e)
                onFailure(e.message ?: "Unknown error")
            }
    }

    // Function to update application status
    fun updateApplicationStatus(userId: String, jobId: String, status: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val applicationRef = db.collection("users")
            .document(userId)
            .collection("jobApplications")
            .document(jobId)

        applicationRef.update("status", status)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error updating application status: ${e.message}", e)
                onFailure(e.message ?: "Unknown error")
            }
    }

    companion object {
        private const val TAG = "JobRepository"
    }
}
