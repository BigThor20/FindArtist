package com.example.findartist.model

import java.io.Serializable


data class ArtistItemList(
    val id: String = "",
    val profilePhotoUrl: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val city: String = "",
    val rate: Float = 0f,
    val phone: String = "",
    val role: String = "",
    val industry: String = "",
    val job: String = "",
    val description: String = "",
    val photos: ArrayList<String> = ArrayList<String>(),
    val ratings:ArrayList<Map<String, String>> = ArrayList<Map<String, String>>()
): Serializable
