package com.sushmoyr.shikhon.backend.data

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatInstance(
    val chatOwners: List<String> = emptyList(),
    val lastModified: String = "",
    @DocumentId
    val id: String = "",
): Parcelable
