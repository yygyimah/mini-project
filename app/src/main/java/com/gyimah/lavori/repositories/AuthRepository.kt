package com.gyimah.lavori.repositories

import android.app.Application
import android.util.Log
import androidx.arch.core.executor.TaskExecutor
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.gyimah.lavori.constants.Constants
import com.gyimah.lavori.listeners.AccountListener
import com.gyimah.lavori.listeners.LoginListener
import com.gyimah.lavori.listeners.RegisterListener
import com.gyimah.lavori.models.User
import com.gyimah.lavori.utils.Session
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    application: Application
) {

    private lateinit var loginListener: LoginListener
    private lateinit var accountListener: AccountListener
    private lateinit var registerListener: RegisterListener

    private var session: Session = Session.getInstance(application)

    fun setLoginListener(loginListener: LoginListener) {
        this.loginListener = loginListener
    }

    fun setAccountListener(accountListener: AccountListener) {
        this.accountListener = accountListener;
    }

    fun setRegisterListener(registerListener: RegisterListener) {
        this.registerListener = registerListener
    }

    fun loginWithEmail(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(TaskExecutors.MAIN_THREAD) { task ->
                val user = task.user

                if (user != null) {
                    getUser(user.uid)
                } else {
                    loginListener.onLoginFailure("Invalid email or password")
                }

            }
            .addOnFailureListener {
                Log.e("LOGIN EXCEPTION", it.message!!)
                loginListener.onLoginFailure("Invalid email or password")
            }
    }

    fun loginWithGoogle(idToken: String, screen: String = "login") {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential)
            .addOnSuccessListener(TaskExecutors.MAIN_THREAD) {
                val user = it.user

                if (user != null) {
                    if (screen == "login") getUser(user.uid)
                    else registerListener.onRegistrationSuccess()
                } else {
                    if (screen == "login") {
                        loginListener.onLoginFailure("Error logging in, please again")
                    } else {
                        registerListener.onRegistrationFailure("Error logging in, please again")
                    }
                }
            }
    }

    fun getUser(userId: String) {
        firebaseFirestore.collection(Constants.USERS)
            .document(userId)
            .get()
            .addOnSuccessListener {

                if (it.exists()) {
                    val user = it.toObject<User>()
                    session.saveUser(user = user!!)

                    loginListener.onLoginSuccess()


                } else {
                    // user details not stored yet
                    loginListener.onAccountNotFound()
                }

            }
            .addOnFailureListener {
                loginListener.onAccountNotFound()
            }
    }


    fun registerWithEmail(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(TaskExecutors.MAIN_THREAD) {
                registerListener.onRegistrationSuccess()
            }
            .addOnFailureListener {
                var message: String? = it.localizedMessage
                if (message != null) {
                    message = "something unexpected happened, please try again"
                }
                registerListener.onRegistrationFailure(message!!)
            }
    }

    fun saveUserInfo(firstname: String, lastname: String, summary: String) {
        val userId = firebaseAuth.currentUser!!.uid
        val user = User(id = userId, firstName = firstname, lastName = lastname, summary = summary)
        firebaseFirestore.collection(Constants.USERS)
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                session.saveUser(user)

                accountListener.onAccountSetupSuccess()
            }
            .addOnFailureListener {
                accountListener.onAccountSetupFailure(it.localizedMessage!!)
            }
    }
}