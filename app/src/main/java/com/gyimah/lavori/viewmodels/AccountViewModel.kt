package com.gyimah.lavori.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gyimah.lavori.listeners.AccountListener
import com.gyimah.lavori.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), AccountListener{

    init {
        authRepository.setAccountListener(this)
    }

    val successState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<String>();

    fun saveUserInfo(firstname: String, lastname: String, summary: String) {
        when {
            firstname.trim().isBlank() -> {
                errorState.postValue("First name cannot be empty")
            }

            lastname.trim().isBlank() -> {
                errorState.postValue("Last name cannot be empty")
            }
            summary.trim().isBlank() -> {
                errorState.postValue("Summary cannot be empty")
            }
            else -> {
                authRepository.saveUserInfo(firstname, lastname, summary)
            }
        }
    }

    override fun onAccountSetupSuccess() {
        successState.postValue(true)
    }

    override fun onAccountSetupFailure(message: String) {
        successState.postValue(false)
        errorState.postValue(message)
    }
}