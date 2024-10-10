package com.example.istjobs.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.nav.Screens
import com.example.istjobs.utils.JobViewModel
import com.example.istjobs.utils.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddDataScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    jobViewModel: JobViewModel
) {
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser

    // If no user is logged in, navigate back to login screen (for safety)
    if (currentUser == null) {
        navController.navigate(Screens.UserLoginScreen.route) {
            popUpTo(Screens.AddDataScreen.route) { inclusive = true }
        }
        return
    }

    // Retrieve the user ID from the current logged-in user
    val userId = currentUser.uid

    // Define state variables for each user profile field
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var qualification by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Define UI layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add User Profile",
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
        Button(
            onClick = {
                // Start loading process
                isLoading = true

                // Create a new UserProfile object
                val userProfile = UserProfile(
                    userId = userId,
                    name = name,
                    gender = gender,
                    address = address,
                    phoneNumber = phoneNumber,
                    qualifications = qualification,
                    experience = experience
                )

                // Save the user profile using SharedViewModel
                sharedViewModel.saveUserProfile(userProfile) { success ->
                    isLoading = false
                    if (success) {
                        // Navigate to dashboard screen on successful save
                        navController.navigate(Screens.UserDashboardScreen.route) {
                            popUpTo(Screens.AddDataScreen.route) { inclusive = true }
                        }
                    } else {
                        // Show error message
                        // Replace with a Snackbar or dialog if necessary
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }

        // Show a loading indicator if data is being saved
        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}
