package com.example.istjobs.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.data.Job
import com.example.istjobs.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun JobItem(job: Job, navController: NavHostController) {
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
                    .addOnFailureListener {
                        // Handle error
                    }
            }) {
                Text("Apply")
            }
        }
    }
}
