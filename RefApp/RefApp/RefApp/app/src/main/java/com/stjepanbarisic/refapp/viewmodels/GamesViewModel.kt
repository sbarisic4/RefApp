package com.stjepanbarisic.refapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.stjepanbarisic.refapp.models.FootballTeam
import com.stjepanbarisic.refapp.models.Game
import com.stjepanbarisic.refapp.models.Referee
import com.stjepanbarisic.refapp.repositories.GameRepository
import com.stjepanbarisic.refapp.repositories.RefereeRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class GamesViewModel(
    private val gameRepository: GameRepository,
    private val refereeRepository: RefereeRepository
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        toastMLD.value = exception.message.toString()
    }

    private val footballTeamsMLD = MutableLiveData(GameRepository.hnlTeams)
    val footballTeamsLD: LiveData<List<FootballTeam>>
        get() = footballTeamsMLD

    private val refereesMLD = MutableLiveData<List<Referee>>()
    val refereesLD: LiveData<List<Referee>>
        get() = refereesMLD

    private val toastMLD = MutableLiveData<String?>()
    val toastLD: LiveData<String?>
        get() = toastMLD

    private val gamesOptionsMLD = MutableLiveData<FirebaseRecyclerOptions<Game>>(
        FirebaseRecyclerOptions.Builder<Game>()
            .setQuery(
                gameRepository.createGameQuery()
            ) { snapshot ->
                val gameId = snapshot.child("gameId").getValue(String::class.java) ?: ""
                val guestTeam = snapshot.child("guestTeam").getValue(String::class.java) ?: ""
                val hostTeam = snapshot.child("hostTeam").getValue(String::class.java) ?: ""
                val startTime = snapshot.child("startTime").getValue(Long::class.java) ?: 0L
                val referee =
                    snapshot.child("referee").getValue(Referee::class.java) ?: Referee()

                Game(
                    gameId = gameId,
                    guestTeam = guestTeam,
                    hostTeam = hostTeam,
                    startTime = startTime,
                    referee = referee
                )
            }
            .build()
    )
    val gamesOptionsLD: LiveData<FirebaseRecyclerOptions<Game>>
        get() = gamesOptionsMLD

    init {
        viewModelScope.launch(exceptionHandler) {
            refereesMLD.value = refereeRepository.getReferees()
        }
    }

    suspend fun createGame(game: Game, refereeName: String) {
        val referee = refereesMLD.value?.find { it.name == refereeName }
        referee?.let { game.referee = it }
        gameRepository.addGame(game)
    }

    fun getFootballTeamByName(footballTeamName: String): FootballTeam? {
        return GameRepository.hnlTeams.find { it.name == footballTeamName }
    }

    fun resetToast() {
        toastMLD.value = null
    }
}