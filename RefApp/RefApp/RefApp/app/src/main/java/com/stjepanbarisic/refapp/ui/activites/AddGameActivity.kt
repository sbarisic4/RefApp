package com.stjepanbarisic.refapp.ui.activites

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.os.bundleOf
import com.stjepanbarisic.refapp.R
import com.stjepanbarisic.refapp.models.Game
import com.stjepanbarisic.refapp.databinding.ActivityAddGameBinding
import com.stjepanbarisic.refapp.ui.dialogs.DATE_SELECTOR_DATE
import com.stjepanbarisic.refapp.ui.dialogs.DATE_SELECTOR_ID
import com.stjepanbarisic.refapp.ui.dialogs.DATE_SELECTOR_TITLE
import com.stjepanbarisic.refapp.ui.dialogs.DateSelectorFragment
import com.stjepanbarisic.refapp.viewmodels.GamesViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


private const val DIALOG_GAME_START_DATE = 1

private const val SELECTED_DATE = "SelectedDate"

class AddGameActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Toast.makeText(this, exception.message.toString(), Toast.LENGTH_SHORT).show()
    }

    private lateinit var binding: ActivityAddGameBinding
    private val gamesViewModel: GamesViewModel by viewModel()
    private var currentDate = GregorianCalendar(Locale.getDefault()).timeInMillis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            currentDate = savedInstanceState.getLong(SELECTED_DATE)
        }

        binding.etDate.setOnClickListener {
            showDateSelector(getString(R.string.pick_game_date), DIALOG_GAME_START_DATE)
        }

        binding.btnAddGame.setOnClickListener {
            if (validateGameInput()) {
                val spFirstTeamValue = binding.spFirstTeam.selectedItem.toString()
                val spSecondTeamValue = binding.spSecondTeam.selectedItem.toString()
                val spRefereeValue = binding.spReferees.selectedItem.toString()
                val matchStartTime = getTimeOfDayInMillis(
                    currentDate,
                    binding.etHour.text.toString().toInt(),
                    binding.etMinute.text.toString().toInt()
                )
                val game = Game(
                    startTime = matchStartTime,
                    hostTeam = spFirstTeamValue,
                    guestTeam = spSecondTeamValue
                )

                CoroutineScope(Main).launch(exceptionHandler) {
                    gamesViewModel.createGame(game, spRefereeValue)
                    finish()
                }
            }
        }

        gamesViewModel.footballTeamsLD.observe(this, { teams ->
            if (!teams.isNullOrEmpty()) {
                setFootballNamesSpinners(teams.map { team -> team.name })
            }
        })

        gamesViewModel.refereesLD.observe(this, { referees ->
            if (!referees.isNullOrEmpty()) {
                setRefereesSpinner(referees.map { referee -> referee.name })
            }
        })

        gamesViewModel.toastLD.observe(this, { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                gamesViewModel.resetToast()
            }
        })

        binding.etDate.setText(getLocalFormattedDate(currentDate, this))
    }


    private fun showDateSelector(title: String, dialogId: Int) {

        val dateSelectorFragment = DateSelectorFragment()

        val date = Date()
        date.time = currentDate

        val arguments = bundleOf(
            DATE_SELECTOR_ID to dialogId,
            DATE_SELECTOR_TITLE to title,
            DATE_SELECTOR_DATE to date
        )

        dateSelectorFragment.arguments = arguments
        dateSelectorFragment.show(supportFragmentManager, "datePicker")
    }

    private fun setFootballNamesSpinners(teamNames: List<String>) {
        val teamNamesAdapter = ArrayAdapter(this, R.layout.spinner_item, teamNames)
        teamNamesAdapter.setDropDownViewResource(R.layout.spinner_item)

        binding.apply {
            spFirstTeam.adapter = teamNamesAdapter
            spSecondTeam.adapter = teamNamesAdapter
        }
    }

    private fun setRefereesSpinner(refereesNames: List<String>) {
        val refereesAdapter = ArrayAdapter(this, R.layout.spinner_item, refereesNames)
        refereesAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spReferees.adapter = refereesAdapter
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        when (view?.tag as Int) {
            DIALOG_GAME_START_DATE -> {
                val calendar = GregorianCalendar()
                calendar.set(year, month, dayOfMonth, 0, 0, 0)
                currentDate = calendar.timeInMillis

                binding.etDate.setText(
                    getLocalFormattedDate(
                        currentDate,
                        this@AddGameActivity
                    )
                )
            }

            else -> throw IllegalArgumentException("Invalid mode when receiving DateSelectorDialog result")
        }
    }

    private fun getLocalFormattedDate(dateInMillis: Long, context: Context): String {
        val dateFormat = DateFormat.getDateFormat(context)
        return dateFormat.format(dateInMillis)
    }

    private fun getTimeOfDayInMillis(dayInMillis: Long, hour: Int, minute: Int): Long {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = dayInMillis
        calendar.set(GregorianCalendar.HOUR_OF_DAY, hour)
        calendar.set(GregorianCalendar.MINUTE, minute)
        return calendar.timeInMillis

    }

    private fun validateGameInput(): Boolean {

        var isValid = true

        if (binding.spFirstTeam.selectedItem == binding.spSecondTeam.selectedItem) {
            isValid = false
            Toast.makeText(this, getString(R.string.pick_valid_teams), Toast.LENGTH_SHORT).show()
        }

        binding.etHour.apply {
            if (text?.isEmpty() == true || text.toString().toInt() > 24) {
                error = getString(R.string.valid_hour_required)
                requestFocus()
                isValid = false
            }
        }

        binding.etMinute.apply {
            if (text?.isEmpty() == true || text.toString().toInt() > 60) {
                error = getString(R.string.valid_minute_required)
                requestFocus()
                isValid = false
            }
        }

        return isValid
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(SELECTED_DATE, currentDate)
    }
}