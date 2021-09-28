package com.stjepanbarisic.refapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Referee(
    var refereeId: String = "",
    var name: String = ""
) : Parcelable

