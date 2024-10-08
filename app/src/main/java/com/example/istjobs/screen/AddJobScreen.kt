package com.example.istjobs.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.data.Job
import com.example.istjobs.utils.JobViewModel
import com.example.istjobs.nav.Screens
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun AddJobScreen(navController: NavHostController, jobViewModel: JobViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var vacancies by remember { mutableStateOf("") }

    // Get current context
    val context = LocalContext.current

    // Function to show DatePicker
    fun showDatePicker(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                if (isStartDate) {
                    startDate = selectedDate
                } else {
                    expiryDate = selectedDate
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Go Back Button
        Button(onClick = {
            navController.navigate(Screens.AdminDashboardScreen.route) {
                popUpTo(Screens.AdminDashboardScreen.route) { inclusive = true }
            }
        }) {
            Text("Go Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Add Job", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Job Title Input
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Job Title") }
        )

        // Job Description Input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Job Description") },
            modifier = Modifier.height(100.dp)
        )

        // Company Input
        OutlinedTextField(
            value = company,
            onValueChange = { company = it },
            label = { Text("Company") }
        )

        // Start Date Input
        OutlinedTextField(
            value = startDate,
            onValueChange = {},
            label = { Text("Start Date") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker(true) }, // Show date picker on click
            readOnly = true // Make it read-only to avoid keyboard pop-up
        )

        // Expiry Date Input
        OutlinedTextField(
            value = expiryDate,
            onValueChange = {},
            label = { Text("Expiry Date") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker(false) }, // Show date picker on click
            readOnly = true // Make it read-only to avoid keyboard pop-up
        )

        // Vacancies Input
        OutlinedTextField(
            value = vacancies,
            onValueChange = { vacancies = it },
            label = { Text("Number of Vacancies") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Generate a unique String ID for the new Job
            val newJob = Job(
                id = UUID.randomUUID().toString(), // Generate a unique ID as a String
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
    }
}
