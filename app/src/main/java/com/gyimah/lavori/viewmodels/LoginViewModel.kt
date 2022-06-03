package com.gyimah.lavori.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gyimah.lavori.listeners.AccountListener
import com.gyimah.lavori.listeners.LoginListener
import com.gyimah.lavori.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), LoginListener {


    val successState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<String>()
    val accountState = MutableLiveData<Int>()

    init {
        authRepository.setLoginListener(this)
    }

    fun loginWithEmail(email: String, password: String) {

        Log.i("LOGIN VIEW MODEL", email)
        Log.i("LOGIN VIEW MODEL", password)

        when {
            email.trim().isBlank() -> {
                errorState.postValue("Email cannot be empty")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                errorState.postValue("Email is invalid")
            }
            password.trim().isBlank() -> {
                errorState.postValue("Password cannot be empty")
            }

            else -> {
                authRepository.loginWithEmail(email, password)
            }
        }
    }

    fun getUser(id: String) {
        authRepository.getUser(id)
    }

    fun loginWithGoogle(idToken: String) {
        authRepository.loginWithGoogle(idToken)
    }

    override fun onLoginSuccess() {
        successState.postValue(true)
    }

    override fun onLoginFailure(message: String) {
        successState.postValue(false)
        errorState.postValue(message)
    }

    override fun onAccountNotFound() {
        accountState.postValue(1)
    }


}