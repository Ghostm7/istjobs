package com.example.istjobs.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.istjobs.utils.JobViewModel
import com.example.istjobs.data.Job
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.istjobs.nav.Screens


@Composable
fun JobListScreen(
    navController: NavController,
    jobViewModel: JobViewModel = viewModel() // Get the JobViewModel instance
) {
    // Observe the jobs StateFlow
    val jobs by jobViewModel.jobs.collectAsState(initial = emptyList())

    // Fetch jobs when the screen is first displayed
    LaunchedEffect(Unit) {
        jobViewModel.fetchJobs()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navigate to the AddJobScreen when the FAB is clicked
                    navController.navigate(Screens.AddJobScreen.route)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Job")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (jobs.isEmpty()) {
                // Display a message if no jobs are available
                Text("No jobs available")
            } else {
                LazyColumn {
                    items(jobs) { job -> // Ensure job is of type Job
                        JobItem(job) // Assuming you have a JobItem Composable
                    }
                }
            }
        }
    }
}

@Composable
fun JobItem(job: Job) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = job.title, style = MaterialTheme.typography.bodyMedium)
            Text(text = job.description, style = MaterialTheme.typography.bodyMedium)
            // Add more job details here
        }
    }
}
