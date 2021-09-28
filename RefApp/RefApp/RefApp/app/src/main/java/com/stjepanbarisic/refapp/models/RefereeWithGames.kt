package com.stjepanbarisic.refapp.models

class RefereeWithGames(
    var referee: Referee = Referee(),
    var games: List<Game> = emptyList()
)