package com.gyimah.lavori.models

data class User(
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var imageUrl: String? = null,
    var summary: String = "",
    var headLine: String = ""
)
