package com.example.istjobs.utils

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.istjobs.data.Application
import com.example.istjobs.data.Job
import com.example.istjobs.data.UserProfile // Make sure to create this data class
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class JobViewModel : ViewModel() {
    // List of jobs and applications to be observed
    private val _jobs = mutableStateListOf<Job>()
    val jobs: List<Job> get() = _jobs

    private val _applications = mutableStateListOf<Application>()
    val applications: List<Application> get() = _applications

    private val db = FirebaseFirestore.getInstance()

    // Function to fetch jobs from Firestore
    fun fetchJobs() {
        db.collection("jobs").get()
            .addOnSuccessListener { documents ->
                _jobs.clear()
                for (document in documents) {
                    val job = document.toObject(Job::class.java)
                    _jobs.add(job)
                }
            }
            .addOnFailureListener { e -> e.printStackTrace() }
    }

    // Function to add a job to Firestore
    fun addJob(job: Job) {
        db.collection("jobs").document(job.id).set(job)
            .addOnSuccessListener { _jobs.add(job) }
            .addOnFailureListener { e -> e.printStackTrace() }
    }

    // Function to fetch applications from Firestore
    fun fetchApplications() {
        db.collection("applications").get()
            .addOnSuccessListener { documents ->
                _applications.clear()
                for (document in documents) {
                    val application = document.toObject(Application::class.java)
                    _applications.add(application)
                }
            }
            .addOnFailureListener { e -> e.printStackTrace() }
    }

    // Function to fetch user profile data
    fun fetchUserProfile(userId: String, callback: (UserProfile?) -> Unit) {
        db.collection("userProfiles").document(userId).get()
            .addOnSuccessListener { document ->
                val userProfile = document?.toObject(UserProfile::class.java)
                callback(userProfile)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                callback(null)
            }
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
        val application = Application(
            userId = userId,
            name = name,
            gender = gender,
            address = address,
            phoneNumber = phoneNumber,
            qualifications = qualifications,
            experience = experience,
            jobId = jobId,
            status = "pending" // Initial status
        )

        db.collection("applications")
            .add(application)
            .addOnSuccessListener { documentReference ->
                println("Application submitted with ID: ${documentReference.id}")
                _applications.add(application) // Optionally add to local state
            }
            .addOnFailureListener { e ->
                println("Error adding application: $e")
            }
    }


    // Function to get applications for a specific user
    fun getApplications(userId: String): List<Application> {
        return _applications.filter { it.userId == userId }
    }

    // Add other functions as needed (e.g., for jobs)
}
