package com.example.istjobs.data

data class Application(
    var id: String = "", // Optional for document ID
    val userId: String? = null ,// Add this field
    var name: String = "",
    var gender: String = "",
    var address: String = "",
    var phoneNumber: String = "",
    var experience: String = "",
    var jobName: String? = null,
    var dateApplied: String? = null, // Add dateApplied field
    var status: String = ""
)


