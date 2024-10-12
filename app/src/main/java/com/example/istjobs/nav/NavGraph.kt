package com.example.istjobs.nav

import JobUpdateScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.istjobs.screen.* // Import all your screens
import com.example.istjobs.utils.JobViewModel // Import JobViewModel
import com.example.istjobs.utils.SharedViewModel // Import SharedViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.istjobs.utils.JobViewModel.AuthenticationUtil.getCurrentUserId

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
            AddDataScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                jobViewModel = jobViewModel
            )
        }
        composable(route = Screens.JobListScreen.route) {
            JobListScreen(navController = navController, jobViewModel = jobViewModel)
        }




        // Add AddJobScreen route
        composable(route = Screens.AddJobScreen.route) {
            AddJobScreen(navController = navController, jobViewModel = jobViewModel)
        }

        composable(route = Screens.SearchJobsScreen.route) {
            SearchJobsScreen(navController = navController, jobViewModel = jobViewModel)
        }

        composable(route = Screens.UserProfileScreen.route) {
            UserProfileScreen(navController)
        }
        composable(route = Screens.AdminFormScreen.route) {
            AdminFormScreen(navController)
        }

        // Add ProfileScreen route
        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController)
        }

        // Add AdminProfileScreen route
        composable(route = Screens.AdminProfileScreen.route) {
            AdminProfileScreen(navController)
        }

        composable(route = Screens.ApplicationConfirmationScreen.route) {
            ApplicationConfirmationScreen(navController = navController)
        }

        // Add HistoryScreen route
        composable(route = Screens.HistoryScreen.route) {
            HistoryScreen(navController = navController, currentUserId = getCurrentUserId())
        }

        // Add AdminCandidatesScreen route
        composable(route = Screens.AdminCandidatesScreen.route) {
            AdminCandidatesScreen(navController = navController, jobViewModel = jobViewModel)
        }

        // Add JobDetailScreen route
        composable(
            route = "${Screens.JobDetailScreen.route}/{jobId}", // Use the correct format for parameters
            arguments = listOf(navArgument("jobId") { type = NavType.StringType }) // Specify jobId argument
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")
            // Make sure to pass jobViewModel to JobDetailScreen
            if (jobId != null) {
                JobDetailScreen(jobId = jobId, jobViewModel = jobViewModel, navController = navController)
            }
        }

        // Add JobUpdateScreen route
        composable(
            route = "${Screens.JobUpdateScreen.route}/{jobId}", // Use the correct format for parameters
            arguments = listOf(navArgument("jobId") { type = NavType.StringType }) // Specify jobId argument
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")
            // Make sure to pass jobViewModel to JobUpdateScreen
            if (jobId != null) {
                JobUpdateScreen(jobId = jobId, jobViewModel = jobViewModel, navController = navController)
            }
        }
    }
}