package com.sushmoyr.shikhon.frontend.main.trainer.tabs

import com.sushmoyr.shikhon.backend.repository.FirebaseRepository

class UserViewModel {
    val repository = FirebaseRepository()

    val userList = repository.getAllUserData()
}