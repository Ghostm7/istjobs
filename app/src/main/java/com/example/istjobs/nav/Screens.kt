package com.example.istjobs.nav

sealed class Screens(val route: String) {
    object InitialScreen : Screens(route = "initial_screen")
    object UserLoginScreen : Screens(route = "user_login") // Changed to UserLoginScreen
    object UserSignupScreen : Screens(route = "user_signup") // User Signup route
    object AdminSignupScreen : Screens(route = "admin_signup") // Admin Signup route
    object AdminLoginScreen : Screens(route = "admin_login") // Admin Login route
    object AdminDashboardScreen : Screens(route = "admin_dashboard") // Admin Dashboard route
    object UserDashboardScreen : Screens(route = "user_dashboard") // Added User Dashboard route
    object DashboardScreen : Screens(route = "dashboard_screen") // General Dashboard route
    object MainScreen : Screens(route = "main_screen") // Main Screen route
    object AddDataScreen : Screens(route = "add_data_screen") // Add Data Screen route
    object GetDataScreen : Screens(route = "get_data_screen") // Get Data Screen route
    object ProfileScreen : Screens(route = "profile_screen") // Profile Screen route
    object AdminProfileScreen : Screens(route = "admin_profile") // Profile Screen route
    object UserProfileScreen : Screens(route = "user_profile")
    object AdminFormScreen : Screens(route = "admin_form")
    object ForgotPasswordScreen : Screens(route = "forgot_password_screen") // Forgot Password Screen route
    object SettingsScreen : Screens(route = "settings_screen") // Settings Screen route
    object SearchJobsScreen : Screens(route = "search_jobs_screen") // New route for Search Jobs screen
    object HistoryScreen : Screens(route = "history_screen") // New route for History screen
    object AddJobScreen : Screens(route = "add_job_screen")
    object ApplicationConfirmationScreen : Screens(route = "application_confirmation_screen")





    // New AdminCandidatesScreen route
    object AdminCandidatesScreen : Screens(route = "admin_candidates")



    // New JobDetailScreen route
    object JobDetailScreen : Screens(route = "job_detail/{jobId}") {
        fun createRoute(jobId: String) = "job_detail/$jobId"
    }

    // New JobListScreen route
    object JobListScreen : Screens(route = "job_list_screen") // Add this line

    // New JobUpdateScreen route
    object JobUpdateScreen : Screens(route = "job_update/{jobId}") {
        fun createRoute(jobId: String) = "job_update/$jobId"
    }


}