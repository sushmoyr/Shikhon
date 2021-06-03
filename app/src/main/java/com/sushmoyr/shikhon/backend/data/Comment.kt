package com.sushmoyr.shikhon.backend.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val uid: String = "",
    val time: String = "",
    val content: String = ""
):Parcelable
