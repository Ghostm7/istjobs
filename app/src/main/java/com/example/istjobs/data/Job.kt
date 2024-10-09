package com.example.istjobs.data

import java.text.SimpleDateFormat
import java.util.Locale

data class Job(
    val id: String = "", // Default value for Firebase serialization
    val title: String = "",
    val company: String = "",
    val vacancies: Int = 0,
    val startDate: String = "",
    val expiryDate: String = "",
    val description: String = ""
) {
    // Optional: Add a method to check if the job is available
    fun isAvailable(): Boolean {
        return vacancies > 0 && !isExpired()
    }

    // Optional: Check if the job has expired
    private fun isExpired(): Boolean {
        // Assuming expiryDate is in a format you can convert to a timestamp
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return try {
            val expiryMillis = formatter.parse(expiryDate)?.time ?: Long.MAX_VALUE
            expiryMillis < System.currentTimeMillis()
        } catch (e: Exception) {
            false // If parsing fails, consider it not expired
        }
    }
}
