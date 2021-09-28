package com.stjepanbarisic.refapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.stjepanbarisic.refapp.R
import com.stjepanbarisic.refapp.models.Referee
import com.stjepanbarisic.refapp.databinding.ActivityAddRefereeBinding
import com.stjepanbarisic.refapp.viewmodels.RefereesViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class AddRefereeActivity : AppCompatActivity() {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Toast.makeText(this, exception.message.toString(), Toast.LENGTH_SHORT).show()
    }

    private lateinit var binding: ActivityAddRefereeBinding
    private val refereesViewModel: RefereesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRefereeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddReferee.setOnClickListener {
            if (validateRefereeInput()) {
                val referee = Referee(name = binding.etName.text.toString())
                CoroutineScope(Main).launch(exceptionHandler) {
                    refereesViewModel.addReferee(referee)
                    finish()
                }
            }
        }

    }

    private fun validateRefereeInput(): Boolean {

        var isValid = true

        binding.etName.apply {
            if (text?.isEmpty() == true) {
                error = getString(R.string.valid_name_required)
                requestFocus()
                isValid = false
            }
        }

        return isValid
    }
}