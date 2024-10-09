package com.example.istjobs.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.nav.Screens

@Composable
fun ApplicationConfirmationScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Application Submitted",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your application has been submitted successfully.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            // Navigate back to the User Dashboard
            navController.navigate(Screens.UserDashboardScreen.route) {
                // Clear the back stack if needed
                popUpTo(Screens.UserDashboardScreen.route) { inclusive = true }
            }
        }) {
            Text("Go to Dashboard")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            // Navigate to the Application History Screen (if you have one)
            navController.navigate(Screens.HistoryScreen.route)
        }) {
            Text("View Application Status")
        }
    }
}
