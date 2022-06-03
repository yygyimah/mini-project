package com.gyimah.lavori.listeners

interface AccountListener {

    fun onAccountSetupSuccess()

    fun onAccountSetupFailure(message: String)

}