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
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UserProfileScreen(navController: NavHostController) {
    val db = remember { FirebaseFirestore.getInstance() }

    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

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

        // Save Button
        Button(onClick = {
            // Handle save logic here
            val userId = "user_id_placeholder" // Replace with the actual user ID
            val userData = hashMapOf(
                "name" to name,
                "gender" to gender,
                "address" to address,
                "phoneNumber" to phoneNumber
            )

            // Save user profile data to Firestore
            db.collection("users").document(userId)
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
