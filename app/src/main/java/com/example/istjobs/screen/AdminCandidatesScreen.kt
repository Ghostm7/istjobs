package com.example.istjobs.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.graphics.Color
import com.example.istjobs.data.Application
import com.example.istjobs.nav.Screens
import com.example.istjobs.utils.JobViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete

@Composable
fun AdminCandidatesScreen(navController: NavHostController, jobViewModel: JobViewModel) {
    val applied = remember { mutableStateListOf<Application>() }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val db = FirebaseFirestore.getInstance()

    var listenerRegistration: ListenerRegistration? by remember { mutableStateOf(null) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        listenerRegistration = db.collection("applied").addSnapshotListener { snapshot, e ->
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

    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration?.remove()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White) // Set background color to white
    ) {
        // Go Back Arrow Button on top left
        IconButton(onClick = {
            navController.navigate(Screens.AdminDashboardScreen.route) {
                popUpTo(Screens.AdminDashboardScreen.route) { inclusive = true }
            }
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Go Back",
                tint = Color(0xFF6200EA) // Purple color for the arrow
            )
        }

        // Centered "Candidates" title
        Text(
            text = "Candidates",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary, // Set text color to primary
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center // Center the title
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar for filtering by name
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), shape = MaterialTheme.shapes.small)
                .padding(8.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, textAlign = TextAlign.Start),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (searchQuery.text.isEmpty()) {
                    Text("Search by name", style = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        } else if (errorMessage != null) {
            BasicText(errorMessage ?: "")
        } else if (applied.isEmpty()) {
            BasicText("No applications found.")
        } else {
            // Scrollable list of candidates filtered by name
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                applied.filter { it.name?.contains(searchQuery.text, ignoreCase = true) == true }.forEach { application ->
                    CandidateItem(application,
                        onStatusChange = { status ->
                            db.collection("applied").document(application.id)
                                .update("status", status)
                                .addOnSuccessListener {
                                    println("Successfully updated status for document ID: ${application.id}")
                                }
                                .addOnFailureListener { e ->
                                    println("Failed to update status for document ID: ${application.id}. Error: ${e.message}")
                                }
                        },
                        onDelete = {
                            db.collection("applied").document(application.id)
                                .delete()
                                .addOnSuccessListener {
                                    println("Successfully deleted document ID: ${application.id}")
                                    applied.remove(application) // Remove the application from the list
                                }
                                .addOnFailureListener { e ->
                                    println("Failed to delete document ID: ${application.id}. Error: ${e.message}")
                                }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun CandidateItem(application: Application, onStatusChange: (String) -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Show applicant's details including job name and date applied
            Text(text = "Name: ${application.name ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Job Applied For: ${application.jobName ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Date Applied: ${application.dateApplied ?: "N/A"}", style = MaterialTheme.typography.bodyMedium) // Display date applied
            Text(text = "Gender: ${application.gender ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Address: ${application.address ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Phone Number: ${application.phoneNumber ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
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
                Spacer(modifier = Modifier.width(8.dp))
                // Delete Button with trash can icon
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red // Change color as needed
                    )
                }
            }
        }
    }
}
