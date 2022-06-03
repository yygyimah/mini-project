package com.gyimah.lavori.models

data class Post(
    var id: String = "",
    var imageUrl: String? = null,
    var content: String = "",
    var user: User? = null
)
