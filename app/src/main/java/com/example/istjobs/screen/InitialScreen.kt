package com.example.istjobs.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.istjobs.R
import com.example.istjobs.nav.Screens

@Composable
fun InitialScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // IST Logo on top
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                painter = painterResource(R.drawable.isticon),
                contentDescription = "IST Logo"
            )
            Spacer(modifier = Modifier.height(20.dp))

            // User Card
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // User Icon
                    Image(
                        painter = painterResource(id = R.drawable.user2),
                        contentDescription = "User Icon",
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // User Button
                    Button(
                        onClick = {
                            navController.navigate(Screens.UserSignupScreen.route)
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .width(150.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "User",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Admin Card
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Admin Icon
                    Image(
                        painter = painterResource(id = R.drawable.admin2),
                        contentDescription = "Admin Icon",
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Admin Button
                    Button(
                        onClick = {
                            navController.navigate(Screens.AdminSignupScreen.route)
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .width(150.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Admin",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // About Icon
            AboutIcon(navController)
        }
    }
}

@Composable
fun AboutIcon(navController: NavHostController) {
    Image(
        painter = painterResource(id = R.drawable.abouticon), // Replace with your About icon drawable
        contentDescription = "About Icon",
        modifier = Modifier
            .size(50.dp) // Adjust size as needed
            .clickable {
                navController.navigate(Screens.AboutScreen.route) // Navigate to AboutScreen
            }
    )
}
