package com.example.istjobs.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.istjobs.screen.*
import com.example.istjobs.utils.SharedViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
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
            AdminLoginScreen(navController = navController,sharedViewModel = sharedViewModel ) // Remove sharedViewModel here
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
            AddDataScreen(navController = navController, sharedViewModel = sharedViewModel)
        }
    }
}
