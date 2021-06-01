package com.sushmoyr.shikhon.backend.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uuid : String = "",
    val name: String = "",
    val email: String = "",
    val accountType: Int = -1,
    val profilePicUri: String = "images/avatar-placeholder.png",
    val coverPhotoUri: String = "images/skill.jpeg",
    val bio: String = "No bio added",
    val gender: String = "Not Specified",
    val birthdate: String = "1990-1-1",
    val contactNo: String = "Not added yet"
): Parcelable
