package com.example.istjobs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.istjobs.nav.NavGraph
import com.example.istjobs.screen.UserLoginScreen
import com.example.istjobs.ui.theme.IstjobsTheme
import com.example.istjobs.utils.JobViewModel
import com.example.istjobs.utils.SharedViewModel

class MainActivity : ComponentActivity() {
    // Using lazy initialization for SharedViewModel and JobViewModel
    private val sharedViewModel: SharedViewModel by viewModels()
    private val jobViewModel: JobViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display support
        enableEdgeToEdge()

        // Set the content view using Jetpack Compose
        setContent {
            // Fetch jobs from Firestore
            jobViewModel.fetchJobs() // Ensure this method is implemented in your JobViewModel

            IstjobsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create the navigation controller
                    val navController = rememberNavController()

                    // Pass the navigation controller and ViewModels to the navigation graph
                    NavGraph(navController = navController, sharedViewModel = sharedViewModel, jobViewModel = jobViewModel)
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SignupScreenPreview() {
        IstjobsTheme {
            // Preview the LoginScreen with a sample NavController and SharedViewModel
            val navController = rememberNavController()
            UserLoginScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
    }
}
