package com.example.istjobs.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminProfileScreen(navController: NavHostController) {
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser

    // If no user is logged in, navigate back to login screen (for safety)
    if (currentUser == null) {
        navController.navigate(Screens.AdminLoginScreen.route) {
            popUpTo(Screens.AdminProfileScreen.route) { inclusive = true }
        }
        return
    }

    // Get the user ID from the authentication object
    val adminId = currentUser.uid

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // Fetch user profile data
    LaunchedEffect(adminId) {
        db.collection("adminProfiles").document(adminId).get() // Ensure correct collection
            .addOnSuccessListener { document ->
                if (document != null) {
                    name = document.getString("name") ?: ""
                    address = document.getString("address") ?: ""
                    phoneNumber = document.getString("phoneNumber") ?: ""
                }
            }
            .addOnFailureListener { e ->
                // Handle error (e.g., show a message)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Ensure the background is white
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center content vertically
    ) {
        // Profile Card
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f) // Set width to 90% of the screen
                .wrapContentHeight()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White), // Ensure card background is white
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally, // Center items inside the card
                verticalArrangement = Arrangement.Center // Center items vertically
            ) {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display User Details
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Name: $name", style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Address: $address", style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Phone Number: $phoneNumber", style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Edit Profile Button
                Button(onClick = {
                    navController.navigate(Screens.AdminFormScreen.route)
                }) {
                    Text("Edit Profile")
                }

                Spacer(modifier = Modifier.height(8.dp)) // Space before the Go Back button

                // Go Back Button
                Button(onClick = {
                    navController.navigate(Screens.AdminDashboardScreen.route) {
                        popUpTo(Screens.AdminDashboardScreen.route) { inclusive = true }
                    }
                }) {
                    Text("Go Back")
                }
            }
        }
    }
}