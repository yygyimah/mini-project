package com.gyimah.lavori.listeners

interface RegisterListener {

    fun onRegistrationSuccess();

    fun onRegistrationFailure(message: String)
}