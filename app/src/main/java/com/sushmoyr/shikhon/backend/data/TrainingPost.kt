package com.sushmoyr.shikhon.backend.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.model.ServerTimestamps
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.utils.Constants
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime

@Parcelize
data class TrainingPost(
    val postId: String = "",
    var user: User = User(),
    val trainingName: String = "",
    val trainingDescription: String = "",
    val trainingLocation: String = "",
    val postTime: String = "2021-06-01T04:01:37.749",
    val photoUris: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val reacts: List<String> = emptyList(),
    val comments: List<Comment> = emptyList()
): Parcelable
