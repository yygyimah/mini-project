package com.gyimah.lavori.listeners

import com.gyimah.lavori.models.User


interface UserListener {

    fun onRetrieveUsersSuccess(users: MutableList<User>)

    fun onRetrieveUsersFailure(message: String)
}