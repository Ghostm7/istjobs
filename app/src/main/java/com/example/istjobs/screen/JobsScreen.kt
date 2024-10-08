package com.example.istjobs.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.istjobs.R
import androidx.compose.ui.platform.LocalContext
import com.example.istjobs.nav.Screens
import java.util.*

@Composable
fun JobsScreen(navController: NavHostController) {
    var jobDescription by remember { mutableStateOf("") }
    var numberOfVacancies by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var startingDate by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }

    // Create a calendar instance
    val calendar = Calendar.getInstance()

    // Starting Date Selection
    val context = LocalContext.current
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year" // Format the date
                startingDate = date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    // Expiry Date Selection
    val expiryDateDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year" // Format the date
                expiryDate = date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Title
            Text(
                text = "Add Job",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = jobDescription,
                onValueChange = { jobDescription = it },
                label = { Text("Job Description") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = numberOfVacancies,
                onValueChange = { numberOfVacancies = it },
                label = { Text("Number of Vacancies") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = companyName,
                onValueChange = { companyName = it },
                label = { Text("Company Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Starting Date Selection
            OutlinedButton(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (startingDate.isEmpty()) "Select Starting Date" else startingDate)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expiry Date Selection
            OutlinedButton(
                onClick = { expiryDateDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (expiryDate.isEmpty()) "Select Expiry Date" else expiryDate)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // Handle job addition logic here
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Job")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Go Back Button
            TextButton(
                onClick = { navController.navigate(Screens.AdminDashboardScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go Back to Admin Dashboard")
            }
        }
    }
}
