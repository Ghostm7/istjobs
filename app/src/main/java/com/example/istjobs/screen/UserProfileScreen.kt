package com.example.istjobs.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
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
fun UserProfileScreen(navController: NavHostController) {
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser

    // If no user is logged in, navigate back to login screen (for safety)
    if (currentUser == null) {
        navController.navigate(Screens.UserLoginScreen.route) {
            popUpTo(Screens.UserProfileScreen.route) { inclusive = true }
        }
        return
    }

    // Replace "user_id_placeholder" with actual user ID from authentication
    val userId = currentUser.uid

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
        db.collection("userProfiles").document(userId).get() // Changed to "userProfiles"
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // If the document exists, retrieve the data
                    name = document.getString("name") ?: ""
                    gender = document.getString("gender") ?: ""
                    address = document.getString("address") ?: ""
                    phoneNumber = document.getString("phoneNumber") ?: ""
                    qualification = document.getString("qualification") ?: ""
                    experience = document.getString("experience") ?: ""
                    loading = false

                    // If user data is present, navigate to dashboard
                    if (name.isNotEmpty() && gender.isNotEmpty() && address.isNotEmpty() &&
                        phoneNumber.isNotEmpty() && qualification.isNotEmpty() &&
                        experience.isNotEmpty()) {
                        navController.navigate(Screens.UserDashboardScreen.route) {
                            popUpTo(Screens.UserProfileScreen.route) { inclusive = true }
                        }
                    }
                } else {
                    // Document does not exist, it's a new user
                    isNewUser = true
                    loading = false
                }
            }
            .addOnFailureListener {
                // Handle error (e.g., show a message)
                loading = false
            }
    }

    // Show loading indicator if loading
    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center // Align the indicator to the center
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

                // Name Input
                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    decorationBox = { innerTextField ->
                        if (name.isEmpty()) {
                            Text("Enter your name", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Gender Input
                BasicTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    decorationBox = { innerTextField ->
                        if (gender.isEmpty()) {
                            Text("Enter your gender", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Address Input
                BasicTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    decorationBox = { innerTextField ->
                        if (address.isEmpty()) {
                            Text("Enter your address", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone Number Input
                BasicTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    decorationBox = { innerTextField ->
                        if (phoneNumber.isEmpty()) {
                            Text("Enter your phone number", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Qualification Input
                BasicTextField(
                    value = qualification,
                    onValueChange = { qualification = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    decorationBox = { innerTextField ->
                        if (qualification.isEmpty()) {
                            Text("Enter your qualification", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Experience Input
                BasicTextField(
                    value = experience,
                    onValueChange = { experience = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    decorationBox = { innerTextField ->
                        if (experience.isEmpty()) {
                            Text("Enter your experience", color = Color.Gray)
                        }
                        innerTextField()
                    }
                )

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
                    db.collection("userProfiles").document(userId) // Changed to "userProfiles"
                        .set(userData)
                        .addOnSuccessListener {
                            navController.navigate(Screens.UserDashboardScreen.route) {
                                // Clear the back stack if needed
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
