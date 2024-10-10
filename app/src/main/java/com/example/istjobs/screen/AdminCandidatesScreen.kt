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
import com.example.istjobs.utils.JobViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

@Composable

fun AdminCandidatesScreen(navController: NavHostController, jobViewModel: JobViewModel) {
    val applied = remember { mutableStateListOf<Application>() } // Changed from applications to applied
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val db = FirebaseFirestore.getInstance()

    // Firestore Listener Registration
    var listenerRegistration: ListenerRegistration? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        // Load applications from the new applied collection
        listenerRegistration = db.collection("applied").addSnapshotListener { snapshot, e -> // Changed from "applications" to "applied"
            loading = false
            if (e != null) {
                errorMessage = "Error fetching applications: ${e.message}"
                return@addSnapshotListener
            }

            applied.clear() // Clear the existing list before adding new ones
            if (snapshot != null) {
                for (doc in snapshot.documents) {
                    val application = doc.toObject(Application::class.java)
                    if (application != null) {
                        application.id = doc.id // Assign the document ID from Firestore
                        applied.add(application) // Add application to the list
                        Log.d("AdminCandidatesScreen", "Fetched application: $application") // Log fetched application
                    }
                }
            }

            if (applied.isEmpty()) {
                errorMessage = "No applications found."
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
        Text(text = "Candidates", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        } else if (errorMessage != null) {
            BasicText(errorMessage ?: "")
        } else if (applied.isEmpty()) {
            BasicText("No applications found.")
        } else {
            applied.forEach { application ->
                CandidateItem(application) { status ->
                    // Update application status in Firestore
                    db.collection("applied").document(application.id) // Changed from "applications" to "applied"
                        .update("status", status)
                        .addOnSuccessListener {
                            println("Successfully updated status for document ID: ${application.id}")
                        }
                        .addOnFailureListener { e ->
                            println("Failed to update status for document ID: ${application.id}. Error: ${e.message}")
                        }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun CandidateItem(application: Application, onStatusChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Show applicant's details
            Text(text = "Name: ${application.name ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Gender: ${application.gender ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Address: ${application.address ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Phone Number: ${application.phoneNumber ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Qualifications: ${application.qualifications ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Experience: ${application.experience ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)

            Text(text = "Status: ${application.status ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(onClick = { onStatusChange("approved") }) {
                    Text("Approve")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onStatusChange("stalled") }) {
                    Text("Stall")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onStatusChange("rejected") }) {
                    Text("Reject")
                }
            }
        }
    }
}