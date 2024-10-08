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
import com.example.istjobs.utils.JobViewModel
import com.example.istjobs.data.Job
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ApplicationFormScreen(navController: NavHostController ,job:Job) {
    // Retrieve the job ID from the navigation arguments
    val jobId = navController.previousBackStackEntry?.arguments?.getString("jobId")

    // Assuming you have a ViewModel or some repository method to fetch job details by ID
    val jobViewModel: JobViewModel = viewModel()
    val job = jobId?.let { jobViewModel.getJobById(it) } // This function should retrieve the job by ID

    if (job == null) {
        Text("Job not found")
        return
    }

    var jobExperience by remember { mutableStateOf("") }
    var cvLink by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Apply for ${job.title}",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Job Experience Input
        BasicTextField(
            value = jobExperience,
            onValueChange = { jobExperience = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                .padding(16.dp),
            decorationBox = { innerTextField ->
                if (jobExperience.isEmpty()) {
                    Text("Enter your job experience", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CV Link Input
        BasicTextField(
            value = cvLink,
            onValueChange = { cvLink = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                .padding(16.dp),
            decorationBox = { innerTextField ->
                if (cvLink.isEmpty()) {
                    Text("Enter your CV link or upload your CV", color = Color.Gray)
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Handle the application submission logic here
            // For example, send jobExperience and cvLink to a ViewModel or repository
            // You might want to save this data to your database or API
            // Then navigate back or show a success message
            navController.popBackStack() // Navigate back after submission
        }) {
            Text("Submit Application")
        }
    }
}

