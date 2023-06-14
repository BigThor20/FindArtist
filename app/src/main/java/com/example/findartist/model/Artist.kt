package com.example.findartist.model

class Artist(
    firstName: String,
    lastName: String,
    role: UserRole,
    email: String,
    city: String,
    private val industry: String,
    private val job: String,
    private val rate: Float,
    private val description: String,
    private val profilePhotoUrl: String,
    private val photos: ArrayList<String>,
    private val ratings: ArrayList<Map<String, String>>
) : User(firstName, lastName, role, email, city) {
    override fun toMap(): HashMap<String, Any> {
        val artistMap = super.toMap()
        artistMap["industry"] = industry
        artistMap["job"] = job
        artistMap["rate"] = rate
        artistMap["description"] = description
        artistMap["profilePhotoUrl"] = profilePhotoUrl
        artistMap["photos"] = photos
        artistMap["ratings"] = ratings
        return artistMap
    }
}