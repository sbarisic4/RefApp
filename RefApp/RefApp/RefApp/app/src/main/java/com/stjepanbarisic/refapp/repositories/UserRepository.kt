package com.stjepanbarisic.refapp.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun logInUserWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    fun logOutUser() {
        firebaseAuth.signOut()
    }

    fun checkIfLoggedIn(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

}