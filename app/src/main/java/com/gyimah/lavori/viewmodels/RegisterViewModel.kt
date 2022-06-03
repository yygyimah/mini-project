package com.gyimah.lavori.viewmodels

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gyimah.lavori.listeners.RegisterListener
import com.gyimah.lavori.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), RegisterListener {

    val successState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<String>()

    init {
        authRepository.setRegisterListener(this)
    }

    fun registerWithEmail(email: String, password: String) {
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
                authRepository.registerWithEmail(email, password)
            }
        }
    }

    fun registerWithGoogle(idToken: String) {
        authRepository.loginWithGoogle(idToken, "register")
    }

    override fun onRegistrationSuccess() {
        successState.postValue(true)
    }

    override fun onRegistrationFailure(message: String) {
        successState.postValue(false)
        errorState.postValue(message)
    }
}