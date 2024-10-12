package com.example.istjobs.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.istjobs.data.Application
import com.example.istjobs.data.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JobViewModel : ViewModel() {
    // StateFlow for jobs and applications
    private val _jobsFlow = MutableStateFlow<List<Job>>(emptyList())
    val jobs = _jobsFlow.asStateFlow() // Publicly expose as StateFlow

    private val _applicationsFlow = MutableStateFlow<List<Application>>(emptyList())
    val applications = _applicationsFlow.asStateFlow() // Publicly expose as StateFlow

    private val db = FirebaseFirestore.getInstance()

    // Fetch jobs from Firestore
    fun fetchJobs() {
        db.collection("jobs").get()
            .addOnSuccessListener { documents ->
                val jobList = documents.map { document -> document.toObject(Job::class.java) }
                _jobsFlow.value = jobList // Update StateFlow with fetched jobs
                Log.d("JobViewModel", "Fetched jobs: ${jobList.size}")
            }
            .addOnFailureListener { e -> Log.e("JobViewModel", "Error fetching jobs: ${e.message}") }
    }

    // Add a job to Firestore
    fun addJob(job: Job) {
        db.collection("jobs").document(job.id).set(job)
            .addOnSuccessListener {
                fetchJobs() // Refresh the job list after adding
                Log.d("JobViewModel", "Job added with ID: ${job.id}")
            }
            .addOnFailureListener { e -> Log.e("JobViewModel", "Error adding job: ${e.message}") }
    }

    // Delete a job from Firestore
    fun deleteJob(jobId: String) {
        db.collection("jobs").document(jobId).delete()
            .addOnSuccessListener {
                Log.d("JobViewModel", "Job deleted with ID: $jobId")
                fetchJobs() // Refresh the job list after deletion
            }
            .addOnFailureListener { e -> Log.e("JobViewModel", "Error deleting job: ${e.message}") }
    }

    // Get a specific job by ID
    fun getJobById(jobId: String): Flow<Job?> {
        return flow {
            try {
                val jobDoc = db.collection("jobs").document(jobId).get().await()
                val job = jobDoc.toObject(Job::class.java)
                emit(job) // Emit the job as Flow
            } catch (e: Exception) {
                Log.e("JobViewModel", "Error getting job by ID: ${e.message}")
                emit(null)
            }
        }
    }

    // Update a specific job by ID with new parameters
    fun updateJob(jobId: String, title: String, description: String, startDate: String, expiryDate: String, vacancies: Int) {
        viewModelScope.launch {
            try {
                // Reference to the job document in Firestore
                val jobDocRef = db.collection("jobs").document(jobId)
                jobDocRef.update(
                    mapOf(
                        "title" to title,
                        "description" to description,
                        "startDate" to startDate,
                        "expiryDate" to expiryDate,
                        "vacancies" to vacancies
                    )
                ).await() // Update job fields in Firestore
                Log.d("JobViewModel", "Job updated with ID: $jobId")
                fetchJobs() // Refresh the job list after updating
            } catch (e: Exception) {
                Log.e("JobViewModel", "Error updating job: ${e.message}")
            }
        }
    }

    // Fetch applications from Firestore
    fun fetchApplications() {
        db.collection("applied").get()
            .addOnSuccessListener { documents ->
                val applicationList = documents.map { document -> document.toObject(Application::class.java) }
                _applicationsFlow.value = applicationList // Update StateFlow with fetched applications
                Log.d("JobViewModel", "Fetched applications: ${applicationList.size}")
            }
            .addOnFailureListener { e -> Log.e("JobViewModel", "Error fetching applications: ${e.message}") }
    }

    // Function to add an application to Firestore
    fun addApplication(
        userId: String,
        name: String,
        gender: String,
        address: String,
        phoneNumber: String,
        qualifications: String,
        experience: String,
        jobId: String
    ) {
        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        val application = Application(
            userId = userId,
            name = name,
            gender = gender,
            address = address,
            phoneNumber = phoneNumber,
            experience = experience,
            status = "pending", // Initial status
            dateApplied = currentDate // Use dateApplied instead of date
        )

        // Log the application data for debugging
        Log.d("JobViewModel", "Adding application: $application")

        // Add the application to the 'applied' collection
        db.collection("applied")
            .add(application)
            .addOnSuccessListener { documentReference ->
                Log.d("JobViewModel", "Application submitted with ID: ${documentReference.id}")
                fetchApplications() // Refresh applications list if necessary
            }
            .addOnFailureListener { e ->
                Log.e("JobViewModel", "Error adding application: ${e.message}") // Log the error message
            }
    }

    // Function to get applications by userId
    fun getApplications(userId: String) {
        db.collection("applied")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val applicationList = documents.map { document -> document.toObject(Application::class.java) }
                _applicationsFlow.value = applicationList // Update the StateFlow directly
                Log.d("JobViewModel", "Fetched applications for user ID $userId: ${applicationList.size}")
            }
            .addOnFailureListener { exception ->
                Log.e("JobViewModel", "Error fetching applications", exception)
            }
    }

    // Function to update application status
    fun updateApplicationStatus(applicationId: String, newStatus: String) {
        db.collection("applied").document(applicationId)
            .update("status", newStatus)
            .addOnSuccessListener {
                Log.d("JobViewModel", "Application status updated to: $newStatus")
                fetchApplications() // Refresh applications list if necessary
            }
            .addOnFailureListener { e ->
                Log.e("JobViewModel", "Error updating application status: ${e.message}")
            }
    }

    object AuthenticationUtil {
        fun getCurrentUserId(): String {
            return FirebaseAuth.getInstance().currentUser?.uid ?: ""
        }
    }
}
