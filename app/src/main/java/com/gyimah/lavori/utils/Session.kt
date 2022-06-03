package com.gyimah.lavori.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.gyimah.lavori.constants.Constants
import com.gyimah.lavori.models.User
import javax.inject.Singleton

@Singleton
class Session private constructor() {

    companion object Singleton {

        private val instance: Session? = null
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var gson: Gson

        fun getInstance(context: Context): Session {
            sharedPreferences = context.applicationContext.getSharedPreferences("lavoir_pref", Context.MODE_PRIVATE)
            gson = Gson()
            return instance ?: synchronized(this) {
                instance ?: Session()
            }
        }

    }

    fun saveUser(user: User) {
        with(sharedPreferences.edit()) {
            putString(Constants.USER, gson.toJson(user))
            putBoolean(Constants.LOGGED_IN, true)
            apply()
        }
    }

    fun getUser(): User? {
        return gson.fromJson(sharedPreferences.getString(Constants.USER, null), User::class.java)
    }

    fun logout() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}
