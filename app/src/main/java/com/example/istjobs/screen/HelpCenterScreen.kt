package com.example.istjobs.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.istjobs.nav.Screens

@Composable
fun HelpCenterScreen(navController: NavHostController) {
    // Wrap Column in a Box to center the content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Set the background color to black
        contentAlignment = Alignment.Center // Center the content
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Help Center",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White // Change text color to white for better contrast
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Troubleshooting Section
            TroubleshootingSection()

            Spacer(modifier = Modifier.height(20.dp))

            // Contact Support Section
            ContactSupportSection(navController)

            Spacer(modifier = Modifier.height(40.dp))

            // Go Back Button
            Button(
                onClick = {
                    navController.popBackStack() // Navigate back to the previous screen
                },
                modifier = Modifier
                    .padding(8.dp)
                    .width(150.dp)
                    .height(40.dp),
            ) {
                Text(
                    text = "Go Back",
                    fontSize = 16.sp,
                    color = Color.White // Change button text color to white
                )
            }
        }
    }
}

@Composable
fun TroubleshootingSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Troubleshooting Tips",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "1. If you are unable to log in, ensure your email and password are correct and your email is verified.",
                fontSize = 14.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "2. If you forget your password, use the 'Forgot Password' option to reset it.",
                fontSize = 14.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "3. Clear the app cache if you're experiencing performance issues or unusual behavior.",
                fontSize = 14.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "4. Ensure you have the latest version of the app for optimal performance.",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ContactSupportSection(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Contact Support",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "For additional help, please contact us:",
                fontSize = 14.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Contact via Phone Button
            Button(
                onClick = {
                    // Here you would initiate a call or open dialer
                    // Example: context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+1234567890")))
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Call Support (+254740806761)")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Contact via Email Button
            Button(
                onClick = {
                    // Navigate to email intent or compose email in another screen
                    // Example: context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@istjobs.com")))
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Email Support (support@messutoezil@gmail.com)")
            }
        }
    }
}
