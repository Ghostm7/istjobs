package com.example.istjobs.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SearchJobsScreen(navController: NavHostController, jobViewModel: JobViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val jobs by jobViewModel.jobs.collectAsState() // Collect jobs as StateFlow

    // Create a scroll state
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState) // Make the Box scrollable
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.Start // Align items to the start (left side)
        ) {
            // Go Back Icon at the top left
            IconButton(onClick = {
                navController.navigate(Screens.UserDashboardScreen.route) {
                    popUpTo(Screens.UserDashboardScreen.route) { inclusive = true }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, // Use the left arrow icon
                    contentDescription = "Go Back",
                    tint = Color(0xFF6c5ce7) // Set the icon color to purple
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Space between arrow and title

            // Centered "Search Jobs" Title
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Search Jobs",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            SearchBar(searchQuery) { query ->
                searchQuery = query
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtered Job List
            val filteredJobs = jobs.filter { job ->
                job.title.contains(searchQuery, ignoreCase = true) || job.company.contains(searchQuery, ignoreCase = true)
            }

            if (filteredJobs.isNotEmpty()) {
                for (job in filteredJobs) {
                    JobCard(job, navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                Text(text = "No jobs found", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}






@Composable
fun GoBackButton(navController: NavHostController) {
    Button(onClick = {
        navController.navigate(Screens.UserDashboardScreen.route) {
            popUpTo(Screens.UserDashboardScreen.route) { inclusive = true }
        }
    }) {
        Text("Go Back")
    }
}

@Composable
fun SearchBar(searchQuery: String, onQueryChange: (String) -> Unit) {
    BasicTextField(
        value = searchQuery,
        onValueChange = onQueryChange,
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

            ApplyButton(job, navController)
        }
    }
}

@Composable
fun ApplyButton(job: Job, navController: NavHostController) {
    Button(onClick = {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid // Get the current user ID

        if (userId != null) {
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
                    val experience = profileData?.get("experience") as? String ?: ""

                    // Create application data
                    val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
                        Date()
                    )
                    val applicationData = hashMapOf(
                        "userId" to userId,
                        "name" to name,
                        "gender" to gender,
                        "address" to address,
                        "phoneNumber" to phoneNumber,
                        "experience" to experience,
                        "jobName" to job.title,
                        "dateApplied" to currentDate,
                        "status" to "pending"
                    )

                    // Save the application data
                    db.collection("applied").add(applicationData)
                        .addOnSuccessListener {
                            navController.navigate(Screens.ApplicationConfirmationScreen.route)
                        }
                        .addOnFailureListener { e ->
                            // Log error or show Snackbar
                            e.printStackTrace()
                        }
                } else {
                    // Handle user profile not found
                    println("User profile data not found.")
                }
            }
        } else {
            // Handle user not logged in
            println("User is not logged in.")
        }
    }) {
        Text("Apply")
    }
}
