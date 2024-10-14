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
import com.example.istjobs.data.Job
import com.example.istjobs.nav.Screens
import com.example.istjobs.utils.JobViewModel

import java.util.*

@Composable
fun AddJobScreen(navController: NavHostController, jobViewModel: JobViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var vacancies by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Set the background of the whole screen to white
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White), // Set Card background to white
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add Job",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary // Set title color to primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Job Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Job Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Job Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Job Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )

                // Company Input
                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Company") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Start Date Input
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Start Date (e.g., 01/01/2024)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Expiry Date Input
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = { Text("Expiry Date (e.g., 01/31/2024)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Vacancies Input
                OutlinedTextField(
                    value = vacancies,
                    onValueChange = { vacancies = it },
                    label = { Text("Number of Vacancies") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Add Job Button
                Button(onClick = {
                    val newJob = Job(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        company = company,
                        startDate = startDate,
                        expiryDate = expiryDate,
                        vacancies = vacancies.toIntOrNull() ?: 0
                    )
                    jobViewModel.addJob(newJob)
                    navController.navigate(Screens.AdminDashboardScreen.route)
                }) {
                    Text("Add Job")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Go Back Button
                Button(onClick = {
                    navController.navigate(Screens.JobListScreen.route) {
                        popUpTo(Screens.JobListScreen.route) { inclusive = true }
                    }
                }) {
                    Text("Go Back")
                }
            }
        }
    }
}

