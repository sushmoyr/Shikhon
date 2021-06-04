package com.sushmoyr.shikhon.backend.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val senderId: String = "",
    val sendTime : String = "",
    val message: String = ""
): Parcelable
