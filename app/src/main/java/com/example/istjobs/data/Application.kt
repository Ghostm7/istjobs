package com.example.istjobs.data

data class Application(
    var id: String = "", // Optional for document ID
    var userId: String = "",
    var name: String = "",
    var gender: String = "",
    var address: String = "",
    var phoneNumber: String = "",
    var qualifications: String = "",
    var experience: String = "",
    var date: String? = null, // Add this field
    var status: String = ""
)


