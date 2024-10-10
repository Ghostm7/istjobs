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
import com.example.istjobs.models.UserData
import kotlinx.coroutines.launch

@Composable
fun GetDataScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    var userId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    // For handling messages
    var message by remember { mutableStateOf("") }

    val context = LocalContext.current // Get the current context
    val coroutineScope = rememberCoroutineScope() // Create a coroutine scope

    Column(modifier = Modifier.fillMaxSize()) {
        // Back button
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

        // Main Layout for Getting User Data
        Column(
            modifier = Modifier
                .padding(horizontal = 60.dp, vertical = 50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // User ID Input Field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    value = userId,
                    onValueChange = { userId = it },
                    label = { Text(text = "User ID") }
                )

                // Get User Data Button
                Button(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .width(100.dp),
                    onClick = {
                        if (userId.isNotEmpty()) {
                            // Launch coroutine to retrieve data
                            coroutineScope.launch {
                                val data = sharedViewModel.retrieveData(userId, context) // Pass context here
                                if (data != null) {
                                    email = data.email
                                    role = data.role
                                    message = "Data retrieved successfully!"
                                } else {
                                    message = "No data found for User ID: $userId"
                                }
                            }
                        } else {
                            Toast.makeText(context, "User ID cannot be empty!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(text = "Get Data")
                }
            }

            // Email Input Field
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") }
            )

            // Role Input Field
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = role,
                onValueChange = { role = it },
                label = { Text(text = "Role") }
            )

            // Save Button
            Button(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),
                onClick = {
                    if (userId.isNotEmpty() && email.isNotEmpty() && role.isNotEmpty()) {
                        // Create UserData object
                        val userData = UserData(
                            email = email,
                            role = role,
                            userId = userId
                        )

                        // Launch coroutine to save data
                        coroutineScope.launch {
                            sharedViewModel.saveData(userData, context) // Pass context here
                            message = "Data saved successfully!"
                            // Reset fields
                            userId = ""
                            email = ""
                            role = ""
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Save")
            }

            // Delete Button
            Button(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                onClick = {
                    if (userId.isNotEmpty()) {
                        // Launch coroutine to delete data
                        coroutineScope.launch {
                            sharedViewModel.deleteData(userId, context) // Pass context here
                            message = "Data deleted successfully!"
                            // Reset fields
                            userId = ""
                            email = ""
                            role = ""
                        }
                    } else {
                        Toast.makeText(context, "User ID cannot be empty for deletion!", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Delete")
            }

            // Message Text
            if (message.isNotEmpty()) {
                Text(text = message, modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}
