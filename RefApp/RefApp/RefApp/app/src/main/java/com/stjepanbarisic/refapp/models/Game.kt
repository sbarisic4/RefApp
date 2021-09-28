package com.stjepanbarisic.refapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
    var gameId: String = "",
    var startTime: Long = 0L,
    var hostTeam: String = "",
    var guestTeam: String = "",
    var referee: Referee = Referee()
) : Parcelable