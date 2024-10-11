package com.example.istjobs.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.data.Application
import com.example.istjobs.nav.Screens
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

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

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Application History", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Go Back Button
        Button(
            onClick = {
                navController.navigate(Screens.UserDashboardScreen.route) {
                    popUpTo(Screens.UserDashboardScreen.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .padding(8.dp) // Reduce padding around the button
        ) {
            Text("Go Back")
        }

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