package com.example.istjobs.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.istjobs.R
import com.example.istjobs.nav.Screens

@Composable
fun AdminDashboardScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // IST Logo on top
            Image(
                modifier = Modifier
                    .size(100.dp) // Increased size of IST logo
                    .clip(CircleShape),
                painter = painterResource(R.drawable.isticon),
                contentDescription = "IST Logo"
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Admin Dashboard",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Create a grid layout for dashboard items
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DashboardItem(
                    imageRes = R.drawable.jobsicon, // Replace with your jobs image
                    label = "Jobs",
                    onClick = { navController.navigate(Screens.UserSignupScreen.route) }
                )
                DashboardItem(
                    imageRes = R.drawable.candidatesicon, // Replace with your candidates image
                    label = "Candidates",
                    onClick = { navController.navigate(Screens.UserSignupScreen.route) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DashboardItem(
                    imageRes = R.drawable.profile, // Replace with your profile image
                    label = "Profile",
                    onClick = { navController.navigate(Screens.ProfileScreen.route) }
                )
                DashboardItem(
                    imageRes = R.drawable.logout, // Replace with your logout image
                    label = "Logout",
                    onClick = { navController.navigate(Screens.InitialScreen.route) } // Navigate to InitialScreen
                )
            }
        }
    }
}

@Composable
fun DashboardItem(imageRes: Int, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = label,
            modifier = Modifier.size(80.dp) // Adjust size as necessary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}
