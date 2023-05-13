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
    val industry: String = "",
    val job: String = "",
    val description: String = ""
): Serializable
