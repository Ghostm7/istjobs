package com.example.istjobs.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.utils.JobViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.istjobs.data.Application


@Composable
fun HistoryScreen(navController: NavHostController) {
    // Get the JobViewModel instance
    val jobViewModel: JobViewModel = viewModel()

    // Get userId
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: return

    // Fetch applications for the current user
    val applications = jobViewModel.getApplications(userId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Application History",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (applications.isEmpty()) {
            Text("No application history found.")
        } else {
            applications.forEach { application ->
                ApplicationItem(application)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ApplicationItem(application: Application) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${application.name}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Gender: ${application.gender}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Address: ${application.address}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Phone Number: ${application.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Qualifications: ${application.qualifications}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Experience: ${application.experience}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Job ID: ${application.jobId}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Status: ${application.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
