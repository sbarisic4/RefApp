package com.stjepanbarisic.refapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stjepanbarisic.refapp.models.RefereeWithGames
import com.stjepanbarisic.refapp.models.Referee
import com.stjepanbarisic.refapp.repositories.GameRepository
import com.stjepanbarisic.refapp.repositories.RefereeRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class RefereesViewModel(
    private val refereeRepository: RefereeRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        toastMLD.value = exception.message.toString()
    }

    private val toastMLD = MutableLiveData<String?>()
    val toastLD: LiveData<String?>
        get() = toastMLD

    private val refereesMLD = MutableLiveData<List<RefereeWithGames>>()
    val refereesLD: LiveData<List<RefereeWithGames>>
        get() = refereesMLD

    init {
        refreshReferees()
    }

    fun refreshReferees() {
        viewModelScope.launch(exceptionHandler) {
            val referees = refereeRepository.getReferees()
            val refereesWithGames = mutableListOf<RefereeWithGames>()
            referees.forEach { referee ->
                val games = gameRepository.getRefereeGames(referee.refereeId)
                refereesWithGames.add(RefereeWithGames(referee = referee, games = games))
            }
            refereesMLD.value = refereesWithGames.toList().sortedByDescending { it.games.size }
        }
    }

    suspend fun addReferee(referee: Referee) {
        refereeRepository.addReferee(referee)
    }

    fun resetToast() {
        toastMLD.value = null
    }
}