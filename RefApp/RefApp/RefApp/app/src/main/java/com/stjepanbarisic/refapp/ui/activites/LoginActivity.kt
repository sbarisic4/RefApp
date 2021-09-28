package com.stjepanbarisic.refapp.ui.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.stjepanbarisic.refapp.R
import com.stjepanbarisic.refapp.databinding.ActivityLoginBinding
import com.stjepanbarisic.refapp.viewmodels.AuthViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val authViewModel: AuthViewModel by viewModel()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Toast.makeText(this, exception.message.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIfUserLoggedIn()

        binding.btnLogin.setOnClickListener {
            if (validateLoginInput()) {
                CoroutineScope(Main).launch(exceptionHandler) {
                    val isLoginSuccessful = authViewModel.logInUserWithEmailAndPassword(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                    if (isLoginSuccessful) {
                        startMainActivity()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.user_not_exists),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkIfUserLoggedIn() {
        val user = authViewModel.checkUserLoggedIn()
        if (user != null) {
            startMainActivity()
        }
    }

    private fun validateLoginInput(): Boolean {

        var isValid = true

        binding.etEmail.apply {
            if (text?.isEmpty() == true) {
                error = getString(R.string.valid_email_required)
                requestFocus()
                isValid = false
            }
        }

        binding.etPassword.apply {
            if (text?.isEmpty() == true) {
                error = getString(R.string.valid_password_required)
                requestFocus()
                isValid = false
            }
        }

        return isValid
    }
}