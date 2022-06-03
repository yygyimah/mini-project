package com.gyimah.lavori.listeners

interface LoginListener {

    fun onLoginSuccess();

    fun onLoginFailure(message: String)

    fun onAccountNotFound()

}