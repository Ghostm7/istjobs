package com.example.istjobs.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.data.Application
import com.example.istjobs.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun HistoryScreen(navController: NavHostController, currentUserId: String) {
    val history = remember { mutableStateListOf<Application>() }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val db = FirebaseFirestore.getInstance()

    // Firestore Listener Registration
    var listenerRegistration: ListenerRegistration? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        // Load history from the user's application history collection
        listenerRegistration = db.collection("applied") // Change to the actual collection name
            .whereEqualTo("userId", currentUserId) // Add a where clause to filter by user ID
            .addSnapshotListener { snapshot, e ->

                loading = false
                if (e != null) {
                    errorMessage = "Error fetching history: ${e.message}"
                    return@addSnapshotListener
                }

                history.clear() // Clear the existing list before adding new ones
                if (snapshot != null) {
                    for (doc in snapshot.documents) {
                        val application = doc.toObject(Application::class.java)
                        if (application != null) {
                            application.id = doc.id // Assign the document ID from Firestore
                            history.add(application) // Add application to the list
                            Log.d("HistoryScreen", "Fetched application: $application") // Log fetched application
                        }
                    }
                }

                if (history.isEmpty()) {
                    errorMessage = "No history found."
                }
            }
    }

    // Cleanup the listener when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration?.remove()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Set background to white
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally // Center contents horizontally
        ) {
            // Go Back Button with Arrow Icon above the title
            IconButton(
                onClick = {
                    navController.navigate(Screens.UserDashboardScreen.route) {
                        popUpTo(Screens.UserDashboardScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Start) // Align to the start of the column
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back", tint = Color(0xFF6200EE)) // Set icon color to purple
            }

            Spacer(modifier = Modifier.height(16.dp)) // Space between arrow and title

            Text(
                text = "Application History",
                style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.primary) // Use primary color from the theme
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                BasicText(errorMessage ?: "")
            } else if (history.isEmpty()) {
                BasicText("No history found.")
            } else {
                history.forEach { application ->
                    HistoryItem(application)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun HistoryItem(application: Application) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${application.name ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Job Applied For: ${application.jobName ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Date Applied: ${application.dateApplied ?: "N/A"}", style = MaterialTheme.typography.bodyMedium) // Display date applied
            Text(text = "Status: ${application.status ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

