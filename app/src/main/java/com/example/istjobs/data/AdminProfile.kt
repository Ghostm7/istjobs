package com.example.istjobs.data

data class AdminProfile(
    val name: String = "",
    val email: String = "",
    val address: String = "",
    val adminId: String = "",
    val phoneNumber: String = "",
    val role: String = "admin",
    val createdAt: Long = System.currentTimeMillis()
)
