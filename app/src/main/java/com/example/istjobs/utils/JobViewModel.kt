package com.example.istjobs.utils

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.istjobs.data.Application
import com.example.istjobs.data.Job
import com.example.istjobs.data.UserProfile // Ensure you have this data class defined
import com.google.firebase.firestore.FirebaseFirestore

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
        db.collection("applied").get() // Ensure you are fetching from the 'applied' collection
            .addOnSuccessListener { documents ->
                _applications.clear()
                for (document in documents) {
                    val application = document.toObject(Application::class.java)
                    _applications.add(application)
                }
            }
            .addOnFailureListener { e -> e.printStackTrace() }
    }
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
            status = "pending", // Initial status
            date = System.currentTimeMillis().toString() // Store the current date as a timestamp, you can format it as needed
        )


        // Log the application data for debugging
        println("Adding application: $application")

        // Add the application to the 'applied' collection
        db.collection("applied")
            .add(application)
            .addOnSuccessListener { documentReference ->
                println("Application submitted with ID: ${documentReference.id}")
                _applications.add(application) // Optionally add to local state
            }
            .addOnFailureListener { e ->
                println("Error adding application: ${e.message}") // Log the error message
            }
    }




        fun getApplications(userId: String): LiveData<List<Application>> {
            val applications = MutableLiveData<List<Application>>()

            db.collection("applied")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val applicationList = documents.map { document ->
                        val application = document.toObject(Application::class.java)
                        application.status = document.getString("status") ?: "pending"
                        application
                    }
                    applications.value = applicationList
                }
                .addOnFailureListener { exception ->
                    Log.e("JobViewModel", "Error fetching applications", exception)
                }

            return applications
        }
    }