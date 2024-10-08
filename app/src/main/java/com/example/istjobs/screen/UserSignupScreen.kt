package com.example.istjobs.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.istjobs.R
import com.example.istjobs.nav.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UserSignupScreen(
    navController: NavHostController
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val db = remember { FirebaseFirestore.getInstance() } // Initialize Firestore instance
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "User Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(R.drawable.isticon), // Replace with your app logo if needed
                    contentDescription = "User Icon",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(56.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it.trim() }, // Trim whitespace
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = passwordVisible,
                        onCheckedChange = { passwordVisible = it }
                    )
                    Text(text = if (passwordVisible) "Hide Password" else "Show Password")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val trimmedEmail = email.trim()  // Trim any whitespace
                        println("Email entered: '$trimmedEmail'")  // Log the exact input

                        // Validate email format
                        if (!isEmailValid(trimmedEmail)) {
                            errorMessage = "Please enter a valid email address."
                            println("Invalid email format")
                            return@Button
                        }

                        // Validate password match
                        if (password.isEmpty() || confirmPassword.isEmpty()) {
                            errorMessage = "Please fill in all fields."
                            return@Button
                        }

                        if (password == confirmPassword) {
                            signUp(auth, db, trimmedEmail, password, { user ->
                                // Navigate to LoginScreen upon successful signup
                                navController.navigate(Screens.UserLoginScreen.route) {
                                    popUpTo(Screens.UserSignupScreen.route) { inclusive = true }
                                }
                            }, { error ->
                                errorMessage = error
                            })
                        } else {
                            errorMessage = "Passwords do not match"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Up")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = { navController.navigate(Screens.UserLoginScreen.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Already have an account? Login")
                }

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

// Basic email validation function
private fun isEmailValid(email: String): Boolean {
    return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

// Firebase sign-up function that adds user data to Firestore
private fun signUp(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    email: String,
    password: String,
    onSuccess: (FirebaseUser?) -> Unit,
    onFailure: (String) -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val userData = hashMapOf(
                    "email" to email,
                    "role" to "user" // Define the role of the user (can be "user" or "admin")
                )

                // Add the user data to Firestore
                user?.let {
                    db.collection("users")
                        .document(it.uid) // Use user's UID as document ID
                        .set(userData)
                        .addOnSuccessListener {
                            onSuccess(user) // Firestore data added successfully
                        }
                        .addOnFailureListener { e ->
                            onFailure("Failed to add user data to Firestore: ${e.message}")
                        }
                }
            } else {
                val errorMessage = task.exception?.message ?: "Sign up failed"
                if (errorMessage.contains("email address is already in use", ignoreCase = true)) {
                    onFailure("This email is already registered. Please log in.")
                } else {
                    onFailure(errorMessage)
                }
            }
        }
}
