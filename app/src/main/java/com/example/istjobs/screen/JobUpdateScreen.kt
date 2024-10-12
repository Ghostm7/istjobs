import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.istjobs.utils.JobViewModel
import kotlinx.coroutines.launch

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

    Column(modifier = Modifier.padding(16.dp)) {
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Job")
        }
    }
}
