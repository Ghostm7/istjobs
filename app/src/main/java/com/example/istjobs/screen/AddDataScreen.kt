package com.example.istjobs.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.istjobs.utils.SharedViewModel
import com.example.istjobs.utils.UserData
import kotlinx.coroutines.launch

@Composable
fun AddDataScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    var userID by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var ageInt by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope() // Create a coroutine scope

    Column(modifier = Modifier.fillMaxSize()) {
        // Back Button Row
        Row(
            modifier = Modifier
                .padding(start = 15.dp, top = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back Button")
            }
        }

        // Add Data Form Layout
        Column(
            modifier = Modifier
                .padding(start = 60.dp, end = 60.dp, bottom = 50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // User ID Field
            OutlinedTextField(
                value = userID,
                onValueChange = { userID = it },
                label = { Text("User ID") },
                modifier = Modifier.fillMaxWidth()
            )

            // Name Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Profession Field
            OutlinedTextField(
                value = profession,
                onValueChange = { profession = it },
                label = { Text("Profession") },
                modifier = Modifier.fillMaxWidth()
            )

            // Age Field
            OutlinedTextField(
                value = age,
                onValueChange = {
                    age = it
                    ageInt = age.toIntOrNull() ?: 0 // Convert age to Int or fallback to 0
                },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Save Button
            Button(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),
                onClick = {
                    // Launch a coroutine to call saveData
                    coroutineScope.launch {
                        // Create a UserData object
                        val userData = UserData(
                            userID = userID,
                            username = username,
                            profession = profession,
                            age = ageInt
                        )

                        // Save data using the sharedViewModel with context
                        try {
                            sharedViewModel.saveData(userData, context) // Include context here
                            Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }

                        // Optionally, navigate back after saving
                        navController.popBackStack() // Navigate back after saving
                    }
                }
            ) {
                Text("Save")
            }
        }
    }
}
