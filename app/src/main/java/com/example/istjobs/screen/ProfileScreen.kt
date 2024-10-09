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
    var qualification by remember { mutableStateOf("") }
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
                    qualification = document.getString("qualification") ?: "" // Fetch qualification
                    experience = document.getString("experience") ?: "" // Fetch experience
                }
            }
            .addOnFailureListener { e ->
                // Handle error (e.g., show a message)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display User Details
        Text("Name: $name", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Gender: $gender", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Address: $address", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Phone Number: $phoneNumber", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Qualification: $qualification", style = MaterialTheme.typography.bodyLarge) // Display qualification
        Spacer(modifier = Modifier.height(8.dp))
        Text("Experience: $experience", style = MaterialTheme.typography.bodyLarge) // Display experience

        Spacer(modifier = Modifier.height(16.dp))

        // Edit Profile Button
        Button(onClick = {
            navController.navigate(Screens.UserProfileScreen.route)
        }) {
            Text("Edit Profile")
        }
    }
}
