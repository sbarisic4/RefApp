package com.stjepanbarisic.refapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FootballTeam(
    val name: String,
    val location: Location
) : Parcelable