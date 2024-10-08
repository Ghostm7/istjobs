package com.example.istjobs.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.istjobs.screen.*
import com.example.istjobs.utils.JobViewModel // Import JobViewModel
import com.example.istjobs.utils.SharedViewModel // Import SharedViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    jobViewModel: JobViewModel // Add jobViewModel parameter here
) {
    NavHost(
        navController = navController,
        startDestination = Screens.InitialScreen.route // Start destination
    ) {
        composable(route = Screens.InitialScreen.route) {
            InitialScreen(navController = navController)
        }

        composable(route = Screens.UserSignupScreen.route) {
            UserSignupScreen(navController = navController)
        }

        composable(route = Screens.AdminSignupScreen.route) {
            AdminSignupScreen(navController = navController)
        }

        composable(route = Screens.AdminLoginScreen.route) {
            AdminLoginScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(route = Screens.UserLoginScreen.route) {
            UserLoginScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(route = Screens.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController = navController)
        }

        composable(route = Screens.UserDashboardScreen.route) {
            UserDashboardScreen(navController = navController)
        }

        composable(route = Screens.AdminDashboardScreen.route) {
            AdminDashboardScreen(navController = navController)
        }

        composable(route = Screens.MainScreen.route) {
            MainScreen(navController = navController)
        }

        composable(route = Screens.GetDataScreen.route) {
            GetDataScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(route = Screens.AddDataScreen.route) {
            AddDataScreen(navController = navController, sharedViewModel = sharedViewModel, jobViewModel = jobViewModel)
        }

        // Add this line to include AddJobScreen
        composable(route = Screens.AddJobScreen.route) {
            AddJobScreen(navController = navController, jobViewModel = jobViewModel)
        }

        composable(route = Screens.SearchJobsScreen.route) {
            SearchJobsScreen(navController = navController, jobViewModel = jobViewModel)
        }

        composable(Screens.UserProfileScreen.route) {
            UserProfileScreen(navController)
        }

        // Add the ApplicationFormScreen composable
        composable(route = Screens.ApplicationFormScreen.route) { backStackEntry ->
            // Retrieve the Job object from the back stack arguments if needed
            val jobId = backStackEntry.arguments?.getString("jobId") // Assuming you pass jobId as an argument
            val job = jobViewModel.jobs.find { it.id.toString() == jobId } // Find job by ID

            // Navigate to ApplicationFormScreen
            job?.let {
                ApplicationFormScreen(navController = navController, job = it)
            }
        }

    }
}
