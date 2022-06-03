package com.gyimah.lavori.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gyimah.lavori.listeners.UserListener
import com.gyimah.lavori.models.User
import com.gyimah.lavori.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(), UserListener {

    init {
        userRepository.setUserListener(this)
    }

    val errorMessageState = MutableLiveData<String>()
    val usersState = MutableLiveData<MutableList<User>>()


    fun getUsers(authId: String) {
        userRepository.getUsers(authId)
    }

    override fun onRetrieveUsersSuccess(users: MutableList<User>) {
        usersState.postValue(users)
    }

    override fun onRetrieveUsersFailure(message: String) {
        errorMessageState.postValue(message)
    }
}