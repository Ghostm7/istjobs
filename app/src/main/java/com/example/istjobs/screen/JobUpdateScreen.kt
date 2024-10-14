import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.istjobs.utils.JobViewModel
import androidx.compose.foundation.background
import androidx.compose.ui.text.style.TextAlign

@Composable
fun JobUpdateScreen(
    navController: NavController,
    jobId: String, // Receive the job ID to be updated
    jobViewModel: JobViewModel = viewModel()
) {
    // Fetch the job data from ViewModel
    val job by jobViewModel.getJobById(jobId).collectAsState(initial = null)

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var vacancies by remember { mutableStateOf("") }

    // Initialize the form with the current job data
    LaunchedEffect(job) {
        job?.let {
            title = it.title
            description = it.description
            startDate = it.startDate ?: ""
            expiryDate = it.expiryDate ?: ""
            vacancies = it.vacancies?.toString() ?: ""
        }
    }

    // Set the background color of the entire screen to white
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White), // Set background color to white
        contentAlignment = Alignment.Center // Align the card to the center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f) // Make card width 90% of the screen
                .wrapContentHeight(), // Height wraps content
            colors = CardDefaults.cardColors(containerColor = Color.White), // Set card background color to white
            elevation = CardDefaults.cardElevation(4.dp) // Add some elevation for shadow effect
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Centered Job Update Title
                Text(
                    text = "Job Update",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary, // Set title color to primary
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Center the title
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Start Date") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = { Text("Expiry Date") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = vacancies,
                    onValueChange = { vacancies = it },
                    label = { Text("Vacancies") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // Space between buttons
                ) {
                    // Update Job Button
                    Button(
                        onClick = {
                            // Convert vacancies to integer
                            val vacancyCount = vacancies.toIntOrNull() ?: 0

                            // Update the job in Firestore using the ViewModel's updateJob function
                            jobViewModel.updateJob(
                                jobId = jobId,
                                title = title,
                                description = description,
                                startDate = startDate,
                                expiryDate = expiryDate,
                                vacancies = vacancyCount // Ensure updateJob function accepts vacancies
                            )
                            navController.popBackStack() // Go back to the previous screen after updating
                        },
                        modifier = Modifier.size(100.dp, 40.dp) // Smaller button size
                    ) {
                        Text("Update")
                    }

                    // Go Back Button
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(100.dp, 40.dp) // Smaller button size
                    ) {
                        Text("Go Back")
                    }
                }
            }
        }
    }
}
