package com.sushmoyr.shikhon.frontend.main.trainer.tabs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.sushmoyr.shikhon.backend.data.ChatInstance
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    val repository = FirebaseRepository

    val userList = repository.getAllUserData()
    val allPost = repository.getPostData()
    val allReviews = repository.getAllReviews()

    suspend fun getRoomId(roomId: String) : Task<DocumentSnapshot> {
        return repository.getSingleRoom(roomId)
    }
    fun createNewRoom(user1: String, user2: String, time: String, roomId: String): ChatInstance {
        val instance = ChatInstance(listOf(user1, user2), time, roomId)
        repository.createNewRoom(instance)
        return instance
    }
}