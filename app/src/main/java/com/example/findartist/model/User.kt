package com.example.findartist.model

open class User(
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val email: String,
    val city: String
) {
    open fun toMap(): HashMap<String, Any> {
        return hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "role" to role,
            "email" to email,
            "city" to city
        )
    }
}

