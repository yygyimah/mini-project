package com.gyimah.lavori.models


data class Job(
    var id: String = "",
    var role: String = "",
    var company: String = "",
    var location: String = "",
    var type: String = "",
    var user: User? = null,
    var postedAt: String = "",

)