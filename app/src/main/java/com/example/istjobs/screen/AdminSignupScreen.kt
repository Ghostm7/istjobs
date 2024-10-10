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
fun AdminSignupScreen(
    navController: NavHostController
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val db = remember { FirebaseFirestore.getInstance() }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSignupSuccessful by remember { mutableStateOf(false) }

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
                    text = "Admin Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(R.drawable.isticon),
                    contentDescription = "Admin Icon",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(56.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    label = { Text("Admin Email") },
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
                        val trimmedEmail = email.trim()
                        if (!isEmailValid(trimmedEmail)) {
                            errorMessage = "Please enter a valid email address."
                            return@Button
                        }

                        if (password.isEmpty() || confirmPassword.isEmpty()) {
                            errorMessage = "Please fill in all fields."
                            return@Button
                        }

                        if (password == confirmPassword) {
                            signUp(auth, db, trimmedEmail, password, "admin", { user ->
                                isSignupSuccessful = true
                                errorMessage = "Sign up successful! Please check your email to verify your account."
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
                    onClick = { navController.navigate(Screens.AdminLoginScreen.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Already have an account? Login")
                }

                errorMessage?.let {
                    Text(
                        text = it,
                        color = if (isSignupSuccessful) Color.Green else MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

private fun isEmailValid(email: String): Boolean {
    return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

private fun signUp(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    email: String,
    password: String,
    role: String,
    onSuccess: (FirebaseUser?) -> Unit,
    onFailure: (String) -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                    if (emailTask.isSuccessful) {
                        val userData = hashMapOf(
                            "email" to email,
                            "role" to role
                        )

                        user.let {
                            db.collection("users").document(it.uid).set(userData)
                                .addOnSuccessListener { onSuccess(user) }
                                .addOnFailureListener { e ->
                                    onFailure("Failed to add admin data to Firestore: ${e.message}")
                                }
                        }
                    } else {
                        onFailure("Failed to send verification email: ${emailTask.exception?.message}")
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
