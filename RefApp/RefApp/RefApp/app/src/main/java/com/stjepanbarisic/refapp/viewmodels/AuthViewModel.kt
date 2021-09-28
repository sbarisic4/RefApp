package com.stjepanbarisic.refapp.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.stjepanbarisic.refapp.repositories.UserRepository

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    suspend fun logInUserWithEmailAndPassword(
        email: String,
        password: String
    ): Boolean {

        val loginUser = userRepository.logInUserWithEmailAndPassword(email, password)?.user
        return loginUser != null
    }

    fun logOut() {
        userRepository.logOutUser()
    }

    fun checkUserLoggedIn(): FirebaseUser? {
        return userRepository.checkIfLoggedIn()
    }

}