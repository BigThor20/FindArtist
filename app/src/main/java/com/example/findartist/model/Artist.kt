package com.example.findartist.model

class Artist(
    firstName: String,
    lastName: String,
    role: UserRole,
    email: String,
    city: String,
    val industry: String,
    val job: String
) : User(firstName, lastName, role, email, city) {
    override fun toMap(): HashMap<String, Any> {
        val artistMap = super.toMap()
        artistMap["industry"] = industry
        artistMap["job"] = job
        return artistMap
    }
}