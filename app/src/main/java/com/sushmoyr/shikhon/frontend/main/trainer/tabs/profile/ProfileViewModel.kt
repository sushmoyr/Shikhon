package com.sushmoyr.shikhon.frontend.main.trainer.tabs.profile

import androidx.lifecycle.ViewModel
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository

class ProfileViewModel: ViewModel() {
    val repository = FirebaseRepository()

    val userList = repository.getAllUserData()
    val allPost = repository.getPostData()
    val allReviews = repository.getAllReviews()
}