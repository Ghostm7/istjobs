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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.istjobs.nav.NavGraph
import com.example.istjobs.screen.UserLoginScreen
import com.example.istjobs.ui.theme.IstjobsTheme
import com.example.istjobs.utils.JobViewModel
import com.example.istjobs.utils.SharedViewModel

class MainActivity : ComponentActivity() {
    // Lazy initialization of the SharedViewModel using viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val jobViewModel: JobViewModel by viewModels() // Add this line to initialize JobViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display support
        enableEdgeToEdge()

        // Setting up the Compose content
        setContent {
            // Applying the app's theme
            IstjobsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create the navigation controller
                    val navController = rememberNavController()

                    // Pass navController and sharedViewModel to the navigation graph
                    NavGraph(navController = navController, sharedViewModel = sharedViewModel, jobViewModel = jobViewModel)
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SignupScreenPreview() {
        IstjobsTheme {
            // Preview the LoginScreen with a sample NavController and ViewModel
            UserLoginScreen(navController = rememberNavController(), sharedViewModel = viewModel())
        }
    }
}
