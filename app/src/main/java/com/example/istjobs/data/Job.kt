package com.example.istjobs.data

data class Job(
    val id: String, // ID is of type String
    val title: String,
    val company: String,
    val vacancies: Int,
    val startDate: String,
    val expiryDate: String,
    val description: String
)
