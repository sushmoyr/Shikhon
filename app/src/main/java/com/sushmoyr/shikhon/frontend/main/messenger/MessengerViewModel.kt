package com.sushmoyr.shikhon.frontend.main.messenger

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository

class MessengerViewModel : ViewModel() {

    private val currentUserId = Firebase.auth.currentUser?.uid.toString()
    val userRooms = FirebaseRepository.getRooms(currentUserId)
    val userList = FirebaseRepository.getAllUserData()

    fun getMessages(roomId: String) = FirebaseRepository.getMessages(roomId)
}