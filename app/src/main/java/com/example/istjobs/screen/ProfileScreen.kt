package com.example.istjobs.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(navController: NavHostController) {
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser

    // If no user is logged in, navigate back to login screen (for safety)
    if (currentUser == null) {
        navController.navigate(Screens.UserLoginScreen.route) {
            popUpTo(Screens.ProfileScreen.route) { inclusive = true }
        }
        return
    }

    // Get the user ID from the authentication object
    val userId = currentUser.uid

    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var qualifications by remember { mutableStateOf("") } // Changed here
    var experience by remember { mutableStateOf("") }

    // Fetch user profile data
    LaunchedEffect(userId) {
        db.collection("userProfiles").document(userId).get() // Ensure correct collection
            .addOnSuccessListener { document ->
                if (document != null) {
                    name = document.getString("name") ?: ""
                    gender = document.getString("gender") ?: ""
                    address = document.getString("address") ?: ""
                    phoneNumber = document.getString("phoneNumber") ?: ""
                    qualifications = document.getString("qualifications") ?: "" // Changed here
                    experience = document.getString("experience") ?: ""
                }
            }
            .addOnFailureListener { e ->
                // Handle error (e.g., show a message)
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center // Center content inside the Box
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White) // Set card background color
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Center content vertically
            ) {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF6c5ce7) // Purple color
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display User Details
                Text("Name: $name", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Gender: $gender", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Address: $address", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Phone Number: $phoneNumber", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Qualifications: $qualifications", style = MaterialTheme.typography.bodyLarge, color = Color.Black) // Changed here
                Spacer(modifier = Modifier.height(8.dp))
                Text("Experience: $experience", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                // Edit Profile Button
                Button(onClick = {
                    navController.navigate(Screens.UserProfileScreen.route)
                }) {
                    Text("Edit Profile", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Go Back Button
                Button(onClick = {
                    navController.navigate(Screens.UserDashboardScreen.route) {
                        popUpTo(Screens.ProfileScreen.route) { inclusive = true }
                    }
                }) {
                    Text("Go Back", color = Color.Black)
                }
            }
        }
    }
}
