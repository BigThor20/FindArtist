package com.example.findartist.model

data class User(
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val email: String,
    val city: String
)