package com.gyimah.lavori.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.gyimah.lavori.utils.Session
import com.gyimah.lavori.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoutingActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var route: String

    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        session = Session.getInstance(applicationContext)

        // Keep the splash screen visible for this Activity
        splashScreen.setKeepOnScreenCondition {true}

        val firebaseUser = firebaseAuth.currentUser

        loginViewModel.getUser(firebaseUser?.uid ?: "")

        loginViewModel.accountState.observe(this) {
            Log.i("ROUTING ACCOUNT", it.toString())
            if (it == 1) {
                clearUser()
                startActivity(Intent(this@RoutingActivity, AuthActivity::class.java))
                finish()
            }
        }

        loginViewModel.successState.observe(this) {
            Log.i("ROUTING SUCCESS", it.toString())
           if (it) {
               startActivity(Intent(this@RoutingActivity, MainActivity::class.java))
           }else {
               clearUser()
               startActivity(Intent(this@RoutingActivity, AuthActivity::class.java))
           }
            finish()
        }


    }

    private fun clearUser() {
        session.logout()
        firebaseAuth.signOut();
    }
}