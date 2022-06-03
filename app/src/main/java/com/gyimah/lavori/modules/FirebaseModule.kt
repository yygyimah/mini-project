package com.gyimah.lavori.modules

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.gyimah.lavori.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideFirebaseUser(auth: FirebaseAuth): FirebaseUser {
        return auth.currentUser!!
    }

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database
    }

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore;
    }

    @Provides
    fun provideFirebaseStore(): FirebaseStorage {
        return Firebase.storage
    }

    @Provides
    fun provideStorageReference(fireStoreStorage: FirebaseStorage): StorageReference {
        return fireStoreStorage.reference
    }

    @Provides
    fun providesGoogleSignInOptions(application: Application): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application.getString(R.string.google_web_client_id))
            .requestEmail()
            .build()
    }

    @Provides
    fun providesGoogleSignInClient(application: Application, googleSignInOptions: GoogleSignInOptions): GoogleSignInClient {
        return GoogleSignIn.getClient(application, googleSignInOptions)
    }
}