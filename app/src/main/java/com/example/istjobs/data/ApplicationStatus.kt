package com.example.istjobs.data

data class ApplicationStatus(
    val jobTitle: String,
    val status: String,
    val submittedOn: String // Or use LocalDateTime for a better date representation
)
