package com.example.istjobs.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.utils.JobViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun JobDetailScreen(jobId: String, jobViewModel: JobViewModel, navController: NavHostController) {
    var job by remember { mutableStateOf<Job?>(null) } // State to hold the job details

    // Fetch the job details using the jobId
    LaunchedEffect(jobId) {
        fetchJobDetails(jobId) { fetchedJob ->
            job = fetchedJob
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show loading state or error handling
        if (job == null) {
            Text("Loading job details...")
        } else {
            job?.let {
                Text(text = "Job Title: ${it.title}", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Description: ${it.description}", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { onApplyForJob(it.id, jobViewModel) }) {
                    Text("Apply for Job")
                }
            }
        }
    }
}

// Function to handle job application
fun onApplyForJob(jobId: String, jobViewModel: JobViewModel) {
    // Get the current user's ID
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    // Fetch the user's profile data
    fetchUserProfile(userId) { userProfile ->
        if (userProfile != null) {
            jobViewModel.addApplication(
                userId = userProfile.userId,
                name = userProfile.name,
                gender = userProfile.gender,
                address = userProfile.address,
                phoneNumber = userProfile.phoneNumber,
                qualifications = userProfile.qualifications,
                experience = userProfile.experience,
                jobId = jobId
            )
        } else {
            println("User profile not found")
        }
    }
}

// Mocking fetchJobDetails for demonstration; replace with your implementation
fun fetchJobDetails(jobId: String, onResult: (Job?) -> Unit) {
    // Implement Firestore fetch logic here
    // This is just a placeholder to demonstrate the flow
    onResult(Job(jobId, "Software Developer", "Develop applications and maintain systems."))
}

// Mocking fetchUserProfile for demonstration; replace with your implementation
fun fetchUserProfile(userId: String, onResult: (UserProfile?) -> Unit) {
    // Implement Firestore fetch logic here
    // This is just a placeholder to demonstrate the flow
    onResult(UserProfile(userId, "John Doe", "Male", "123 Main St", "555-1234", "BSc", "2 years"))
}

// Sample data classes; adjust according to your models
data class Job(
    val id: String,
    val title: String,
    val description: String
)

data class UserProfile(
    val userId: String,
    val name: String,
    val gender: String,
    val address: String,
    val phoneNumber: String,
    val qualifications: String,
    val experience: String
)