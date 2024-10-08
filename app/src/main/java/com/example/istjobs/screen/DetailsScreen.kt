package com.example.istjobs.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DetailsScreen(navController: NavController, title: String, content: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Details of $title",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(text = content, modifier = Modifier.padding(bottom = 16.dp))

        // Additional information based on title or content
        when (title) {
            "Today's Jobs" -> Text(text = "Here you can view all the jobs added today.")
            "Recent Applications" -> Text(text = "View details of recent applications.")
            "Pending Approvals" -> Text(text = "Check the pending job applications.")
            "Active Listings" -> Text(text = "Explore the currently active job listings.")
            "Closed Listings" -> Text(text = "View job listings that have been closed recently.")
            "New Messages" -> Text(text = "Check your new messages from employers.")
            "User Activity" -> Text(text = "View user activity and engagement.")
            "Upcoming Deadlines" -> Text(text = "Keep track of job listings expiring soon.")
        }

        // Back button to go back to Dashboard
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Go Back to Dashboard")
        }
    }
}
