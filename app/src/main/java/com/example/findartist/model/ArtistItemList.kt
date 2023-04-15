package com.example.findartist.model


data class ArtistItemList(
    val profilePhotoUrl: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val city: String = "",
    val rate: Float = 0f,
    val industry: String = "",
    val job: String = "",
    val description: String = ""

)
