package com.stjepanbarisic.refapp.repositories

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.stjepanbarisic.refapp.models.FootballTeam
import com.stjepanbarisic.refapp.models.Location
import com.stjepanbarisic.refapp.models.Game
import kotlinx.coroutines.tasks.await

private const val GAMES_ROOT_NAME = "Games"

class GameRepository {

    private val firebaseInstance = FirebaseDatabase.getInstance()

    suspend fun addGame(game: Game) {
        val gamesReference = firebaseInstance.getReference(GAMES_ROOT_NAME).push()
        val id = gamesReference.key
        game.gameId = id.toString()
        gamesReference.setValue(game).await()
    }

    fun createGameQuery(): Query {
        return firebaseInstance.getReference(GAMES_ROOT_NAME)
    }

    suspend fun getRefereeGames(refereeId: String): List<Game> {
        val refereeGames = mutableListOf<Game>()
        val gamesData = firebaseInstance
            .getReference(GAMES_ROOT_NAME)
            .orderByChild("referee/refereeId")
            .equalTo(refereeId).get().await()
        for (child in gamesData.children) {
            val game = child.getValue(Game::class.java)
            if (game != null) {
                refereeGames.add(game)
            }
        }
        return refereeGames
    }

    companion object {

        private val slogaNovaGradiska = FootballTeam(
            name = "Sloga Nova Gradiška",
            location = Location(45.2639466154947, 17.37713276562144)
        )

        private val oriolik = FootballTeam(
            name = "Oriolik",
            location = Location(45.16355395704699, 17.748903255820892)
        )

        private val NASK = FootballTeam(
            name = "NAŠK",
            location = Location(45.48585685948307, 18.090630969324465)
        )

        private val granicarZupanja = FootballTeam(
            name = "Graničar Županja",
            location = Location(45.07652003473748, 18.692456960362104)
        )

        private val slavonija = FootballTeam(
            name = "Slavonija",
            location = Location(45.33682646493307, 17.672993201349414)
        )

        private val cepin = FootballTeam(
            name = "Čepin",
            location = Location(45.52889890079592, 18.563480769325768)
        )

        private val kutjevo = FootballTeam(
            name = "Kutjevo",
            location = Location(45.416688207154415, 17.880745326993114)
        )

        private val vuteksSloga = FootballTeam(
            name = "Vuteks Sloga",
            location = Location(45.34225502180561, 19.00219705582628)
        )

        private val bedem = FootballTeam(
            name = "Bedem",
            location = Location(45.28426431218714, 18.691623613495224)
        )

        private val darda = FootballTeam(
            name = "Darda",
            location = Location(45.62327884836337, 18.68564885583481)
        )

        private val marsonia = FootballTeam(
            name = "Marsonia",
            location = Location(45.150483210350195, 18.021674471162058)
        )

        private val slavijaPleternica = FootballTeam(
            name = "Slavija Pleternica",
            location = Location(45.28878319306123, 17.803203903697096)
        )

        private val slavonacBukovlje = FootballTeam(
            name = "Slavonac Bukovlje",
            location = Location(45.18995344731502, 18.061880938590555)
        )

        private val omladinacGornjaVrba = FootballTeam(
            name = "Omladinac Gornja Vrba",
            location = Location(45.15246312220827, 18.063514371124473)
        )

        private val belisce = FootballTeam(
            name = "Belišće",
            location = Location(45.68598099490515, 18.40882386274242)
        )

        private val zrinskiJurjevac = FootballTeam(
            name = "Zrinski Jurjevac",
            location = Location(45.44515544185734, 18.444815555528134)
        )

        private val vukovar1991 = FootballTeam(
            name = "Vukovar 1991",
            location = Location(45.380338509939946, 18.965397315345907)
        )

        private val croatiaDakovo = FootballTeam(
            name = "Croatia Đakovo",
            location = Location(45.32123827858722, 18.408441032566955)
        )

        val hnlTeams = listOf(
            slogaNovaGradiska,
            oriolik,
            NASK,
            granicarZupanja,
            slavonija,
            cepin,
            kutjevo,
            vuteksSloga,
            bedem,
            darda,
            marsonia,
            slavijaPleternica,
            slavonacBukovlje,
            omladinacGornjaVrba,
            belisce,
            zrinskiJurjevac,
            vukovar1991,
            croatiaDakovo
        )
    }
}