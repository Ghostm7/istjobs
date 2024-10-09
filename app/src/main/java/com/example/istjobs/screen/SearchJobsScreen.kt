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
                // Save the application to Firestore
                val applicationData = hashMapOf(
                    "jobId" to job.id,
                    "userId" to "user_id_placeholder", // Replace with the actual user ID
                    "status" to "pending" // Set initial status
                )

                val db = FirebaseFirestore.getInstance()
                db.collection("applications").add(applicationData)
                    .addOnSuccessListener {
                        // Navigate to a confirmation screen or show a Snackbar
                        navController.navigate(Screens.ApplicationConfirmationScreen.route)
                    }
                    .addOnFailureListener { e ->
                        // Handle error
                        e.printStackTrace() // Log the error for debugging
                    }
            }) {
                Text("Apply")
            }
        }
    }
}
