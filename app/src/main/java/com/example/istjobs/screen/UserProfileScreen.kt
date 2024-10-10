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

@OptIn(ExperimentalMaterial3Api::class) // Opt-in annotation for experimental APIs
@Composable
fun UserProfileScreen(navController: NavHostController) {
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser

    // If no user is logged in, navigate back to login screen
    if (currentUser == null) {
        navController.navigate(Screens.UserLoginScreen.route) {
            popUpTo(Screens.UserProfileScreen.route) { inclusive = true }
        }
        return
    }

    val userId = currentUser.uid

    // State variables
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var qualification by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }
    var isNewUser by remember { mutableStateOf(false) }

    // Load user profile data when the screen is first displayed
    LaunchedEffect(Unit) {
        db.collection("userProfiles").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    name = document.getString("name") ?: ""
                    gender = document.getString("gender") ?: ""
                    address = document.getString("address") ?: ""
                    phoneNumber = document.getString("phoneNumber") ?: ""
                    qualification = document.getString("qualification") ?: ""
                    experience = document.getString("experience") ?: ""
                    loading = false

                    // Navigate to dashboard if profile is complete
                    if (name.isNotEmpty() && gender.isNotEmpty() && address.isNotEmpty() &&
                        phoneNumber.isNotEmpty() && qualification.isNotEmpty() &&
                        experience.isNotEmpty()) {
                        navController.navigate(Screens.UserDashboardScreen.route) {
                            popUpTo(Screens.UserProfileScreen.route) { inclusive = true }
                        }
                    }
                } else {
                    isNewUser = true
                    loading = false
                }
            }
            .addOnFailureListener {
                loading = false
            }
    }

    // Show loading indicator if loading
    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Render the UI for editing profile if new user or incomplete profile
        if (isNewUser || name.isEmpty() || gender.isEmpty() || address.isEmpty() ||
            phoneNumber.isEmpty() || qualification.isEmpty() || experience.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "User Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input Fields
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = address, onValueChange = { address = it }, label = { Text("Address") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Phone Number") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = qualification, onValueChange = { qualification = it }, label = { Text("Qualification") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = experience, onValueChange = { experience = it }, label = { Text("Experience") })
                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(onClick = {
                    // Handle save logic here
                    val userData = hashMapOf(
                        "name" to name,
                        "gender" to gender,
                        "address" to address,
                        "phoneNumber" to phoneNumber,
                        "qualification" to qualification,
                        "experience" to experience
                    )

                    // Save user profile data to Firestore under "userProfiles"
                    db.collection("userProfiles").document(userId)
                        .set(userData)
                        .addOnSuccessListener {
                            navController.navigate(Screens.UserDashboardScreen.route) {
                                popUpTo(Screens.UserProfileScreen.route) { inclusive = true }
                            }
                        }
                        .addOnFailureListener { e ->
                            // Handle the error (e.g., show a message)
                        }
                }) {
                    Text("Save Profile")
                }
            }
        }
    }
}
