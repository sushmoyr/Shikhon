package com.sushmoyr.shikhon.frontend.auths

import androidx.lifecycle.ViewModel
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository

class AuthViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    fun registerUser(user: User)
    {
        firebaseRepository.addUserToDatabase(user)
    }
}