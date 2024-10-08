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
fun GetDataScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    var userID by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var ageInt by remember { mutableStateOf(0) }

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
            // userID Input Field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    value = userID,
                    onValueChange = { userID = it },
                    label = { Text(text = "UserID") }
                )

                // Get User Data Button
                Button(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .width(100.dp),
                    onClick = {
                        if (userID.isNotEmpty()) {
                            // Launch coroutine to retrieve data
                            coroutineScope.launch {
                                val data = sharedViewModel.retrieveData(userID, context) // Pass context here
                                data?.let {
                                    username = it.username
                                    profession = it.profession
                                    age = it.age.toString()
                                    ageInt = it.age
                                    message = "Data retrieved successfully!"
                                } ?: run {
                                    message = "No data found for UserID: $userID"
                                }
                            }
                        } else {
                            Toast.makeText(context, "UserID cannot be empty!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(text = "Get Data")
                }
            }

            // Name Input Field
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Name") }
            )

            // Profession Input Field
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = profession,
                onValueChange = { profession = it },
                label = { Text(text = "Profession") }
            )

            // Age Input Field
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = age,
                onValueChange = {
                    age = it
                    ageInt = if (age.isNotEmpty() && age.all { char -> char.isDigit() }) {
                        age.toInt()
                    } else {
                        0
                    }
                },
                label = { Text(text = "Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Save Button
            Button(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),
                onClick = {
                    if (userID.isNotEmpty() && username.isNotEmpty() && profession.isNotEmpty() && ageInt > 0) {
                        // Create UserData object
                        val userData = UserData(
                            userID = userID,
                            username = username,
                            profession = profession,
                            age = ageInt
                        )

                        // Launch coroutine to save data
                        coroutineScope.launch {
                            sharedViewModel.saveData(userData, context) // Pass context here
                            message = "Data saved successfully!"
                            // Reset fields
                            userID = ""
                            username = ""
                            profession = ""
                            age = ""
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
                    if (userID.isNotEmpty()) {
                        // Launch coroutine to delete data
                        coroutineScope.launch {
                            sharedViewModel.deleteData(userID, context) // Pass context here
                            message = "Data deleted successfully!"
                            // Reset fields
                            userID = ""
                            username = ""
                            profession = ""
                            age = ""
                        }
                    } else {
                        Toast.makeText(context, "UserID cannot be empty for deletion!", Toast.LENGTH_SHORT).show()
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
