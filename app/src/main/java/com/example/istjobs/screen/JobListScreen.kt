package com.example.istjobs.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.istjobs.utils.JobViewModel
import com.example.istjobs.data.Job
import com.example.istjobs.nav.Screens
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun JobListScreen(
    navController: NavController,
    jobViewModel: JobViewModel = viewModel() // Get the JobViewModel instance
) {
    // State for the search query
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filteredJobs by remember { mutableStateOf<List<Job>>(emptyList()) }

    // Observe the jobs StateFlow
    val jobs by jobViewModel.jobs.collectAsState(initial = emptyList())

    // Fetch jobs when the screen is first displayed
    LaunchedEffect(Unit) {
        jobViewModel.fetchJobs()
    }

    // Update filtered jobs based on search query
    LaunchedEffect(searchQuery) {
        filteredJobs = if (searchQuery.text.isEmpty()) {
            jobs
        } else {
            jobs.filter { job ->
                job.title.contains(searchQuery.text, ignoreCase = true) ||
                        job.description.contains(searchQuery.text, ignoreCase = true)
            }
        }
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color.White) // Set background to white
        ) {
            // Go Back Button with Arrow Icon
            IconButton(
                onClick = {
                    navController.navigate(Screens.AdminDashboardScreen.route) {
                        popUpTo(Screens.AdminDashboardScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Start) // Align to the start of the column
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back", tint = Color(0xFF6200EE)) // Set icon color to purple
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Job List Title
            Text(
                text = "Job List",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp // Adjust the font size if needed
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center // Center the title
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            Box(modifier = Modifier.fillMaxWidth()) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { newValue -> searchQuery = newValue },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                        .padding(16.dp), // Inner padding for the text field
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = Color.Black)
                )
                // Placeholder text
                if (searchQuery.text.isEmpty()) {
                    Text(
                        text = "Search Jobs by title and company",
                        style = LocalTextStyle.current.copy(color = Color.Gray),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp, top = 12.dp) // Adjust padding as needed
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredJobs.isEmpty()) {
                // Display a message if no jobs are available
                Text("No jobs available")
            } else {
                LazyColumn {
                    items(filteredJobs) { job -> // Display filtered jobs
                        JobItem(job = job, navController = navController, jobViewModel = jobViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun JobItem(job: Job, navController: NavController, jobViewModel: JobViewModel) {
    var showDialog by remember { mutableStateOf(false) } // To show/hide the confirmation dialog

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Job details
            Column {
                Text(text = job.title, style = MaterialTheme.typography.bodyMedium)
                Text(text = job.description, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Start Date: ${job.startDate}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Expiry Date: ${job.expiryDate}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Vacancies: ${job.vacancies}", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Buttons for update and delete actions
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Update button
                Button(
                    onClick = {
                        // Navigate to JobUpdateScreen with jobId
                        navController.navigate("${Screens.JobUpdateScreen.route}/${job.id}")
                    }
                ) {
                    Text("Update")
                }

                // Trash can icon for deleting
                IconButton(
                    onClick = { showDialog = true } // Show the confirmation dialog when clicked
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Job")
                }
            }
        }
    }

    // Show confirmation dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Delete Job") },
            text = { Text("Are you sure you want to delete this job?") },
            confirmButton = {
                Button(
                    onClick = {
                        jobViewModel.deleteJob(job.id) // Call delete function from ViewModel
                        showDialog = false // Dismiss the dialog
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false } // Just dismiss the dialog
                ) {
                    Text("No")
                }
            }
        )
    }
}
