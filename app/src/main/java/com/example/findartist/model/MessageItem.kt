package com.example.findartist.model

import java.io.Serializable

data class MessageItem (
    val content: String = "",
    val sender: String = "",
    val date: String = ""
): Serializable
