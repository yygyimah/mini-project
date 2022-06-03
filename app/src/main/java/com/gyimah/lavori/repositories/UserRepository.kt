package com.gyimah.lavori.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.gyimah.lavori.constants.Constants
import com.gyimah.lavori.listeners.UserListener
import com.gyimah.lavori.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    private lateinit var userListener: UserListener

    fun setUserListener(userListener: UserListener) {
        this.userListener = userListener
    }


    fun getUsers(authId: String) {
        val users = mutableListOf<User>()
        firebaseFirestore.collection(Constants.USERS)
            .whereNotEqualTo("id",  authId)
            .addSnapshotListener {snapshot, e ->
                if (e != null) {
                    Log.e("USERS REPO", e.message!!)
                    userListener.onRetrieveUsersFailure("Error fetching users")
                }

                if (snapshot != null) {

                    users.clear()

                    for (item in snapshot.documents) {

                        val user = item.toObject<User>()

                        Log.i("User", user.toString())

                        if (user != null) {
                            users.add(user)
                        }

                    }
                }

                userListener.onRetrieveUsersSuccess(users)
            }

    }
}