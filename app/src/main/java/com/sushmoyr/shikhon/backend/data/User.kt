package com.sushmoyr.shikhon.backend.data

data class User(
    val uuid : String = "",
    val name: String = "",
    val email: String = "",
    val accountType: Int = -1,
    val profilePicUri: String = "https://i.ibb.co/JqKdFpz/avatar-placeholder.png",
    val coverPhotoUri: String = "",
)
