package com.example.istjobs.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.istjobs.R
import com.example.istjobs.nav.Screens

@Composable
fun AboutScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Icon and Name
        AppHeader()

        Spacer(modifier = Modifier.height(20.dp))

        // App Info Section
        AppInfoSection()

        Spacer(modifier = Modifier.height(20.dp))

        // Developer Info Section
        DeveloperInfoSection()

        Spacer(modifier = Modifier.height(20.dp))

        // Help and Contact Section
        HelpContactSection(navController)

        Spacer(modifier = Modifier.height(40.dp))

        // Go Back Button
        Button(
            onClick = {
                navController.popBackStack() // Navigate back to InitialScreen
            },
            modifier = Modifier
                .padding(8.dp)
                .width(150.dp)
                .height(40.dp)
        ) {
            Text(
                text = "Go Back",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun AppHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.isticon), // App icon
            contentDescription = "App Icon",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "ISTJobs",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Version 1.0.0",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun AppInfoSection() {
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
                text = "App Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "ISTJobs is a job application platform designed to help alumnis apply for jobs and allow admins to manage job applications efficiently.",
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Privacy Policy",
                fontSize = 16.sp,
                color = Color.Blue
            ) // You can turn this into a clickable link if you implement a web browser intent.
        }
    }
}

@Composable
fun DeveloperInfoSection() {
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
                text = "Developer Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Developed by: Mohamed Abdisalaan",
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Contact Email: support@messutoezil.com",
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Website: www.istjobs.com",
                fontSize = 14.sp,
                color = Color.Blue // Make this clickable to open the website in a browser.
            )
        }
    }
}

@Composable
fun HelpContactSection(navController: NavHostController) {
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
                text = "Help & Support",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "For any issues, please reach out to our help center or call us at +254740806761.",
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    // Navigate to a help center screen or initiate a call
                    navController.navigate(Screens.HelpCenterScreen.route)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                Text("Go to Help Center")
            }
        }
    }
}
