package com.example.istjobs.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.data.Job
import com.example.istjobs.nav.Screens
import com.example.istjobs.utils.JobViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth


@Composable
fun SearchJobsScreen(navController: NavHostController, jobViewModel: JobViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Go Back Button
        Button(onClick = {
            navController.navigate(Screens.UserDashboardScreen.route) {
                popUpTo(Screens.UserDashboardScreen.route) { inclusive = true }
            }
        }) {
            Text("Go Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Search Jobs",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                .padding(16.dp),
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text("Search jobs by title or company", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtered Job List
        val filteredJobs = jobViewModel.jobs.filter { job ->
            job.title.contains(searchQuery, ignoreCase = true) || job.company.contains(searchQuery, ignoreCase = true)
        }

        if (filteredJobs.isNotEmpty()) {
            for (job in filteredJobs) {
                JobCard(job, navController) // Renamed to JobCard for clarity
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Text(text = "No jobs found", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun JobCard(job: Job, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = job.company, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Vacancies: ${job.vacancies}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Start Date: ${job.startDate}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Expiry Date: ${job.expiryDate}", style = MaterialTheme.typography.bodySmall)
            Text(text = job.description, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                // Get the current user ID from Firebase Auth
                val auth = FirebaseAuth.getInstance()
                val userId = auth.currentUser?.uid // Get the current user ID

                // Check if userId is null, indicating that the user is not logged in
                if (userId != null) {
                    // Get the current user's profile data from Firestore
                    val db = FirebaseFirestore.getInstance()
                    val userProfileRef = db.collection("userProfiles").document(userId)
                    userProfileRef.get().addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val profileData = document.data

                            // Extract the necessary fields from the profile data
                            val name = profileData?.get("name") as? String ?: ""
                            val gender = profileData?.get("gender") as? String ?: ""
                            val address = profileData?.get("address") as? String ?: ""
                            val phoneNumber = profileData?.get("phoneNumber") as? String ?: ""
                            val qualifications = profileData?.get("qualifications") as? String ?: ""
                            val experience = profileData?.get("experience") as? String ?: ""

                            // Create the application data with all the necessary fields
                            val applicationData = hashMapOf(
                                "userId" to userId, // Use the actual user ID
                                "name" to name, // Use the extracted name
                                "gender" to gender, // Use the extracted gender
                                "address" to address, // Use the extracted address
                                "phoneNumber" to phoneNumber, // Use the extracted phone number
                                "qualifications" to qualifications, // Use the extracted qualifications
                                "experience" to experience, // Use the extracted experience
                                "status" to "pending" // Set initial status
                            )

                            // Save the application data to the 'applied' collection
                            db.collection("applied").add(applicationData)
                                .addOnSuccessListener {
                                    // Navigate to a confirmation screen or show a Snackbar
                                    navController.navigate(Screens.ApplicationConfirmationScreen.route)
                                }
                                .addOnFailureListener { e ->
                                    // Handle error
                                    e.printStackTrace() // Log the error for debugging
                                }
                        } else {
                            // Handle case where user profile data is not found
                            println("User  profile data not found.")
                            // You might want to show a Snackbar or Toast here to inform the user
                        }
                    }
                } else {
                    // Handle case where user is not logged in
                    println("User  is not logged in.")
                    // You might want to show a Snackbar or Toast here to inform the user
                }
            }) {
                Text("Apply")
            }

        }
    }
}