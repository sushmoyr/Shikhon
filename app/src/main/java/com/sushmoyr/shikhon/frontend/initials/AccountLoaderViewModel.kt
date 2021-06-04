package com.sushmoyr.shikhon.frontend.initials

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountLoaderViewModel: ViewModel() {

    private val firebaseRepository = FirebaseRepository

    fun getCurrentUserData(uid: String): User{
        var currentUser = User()
         viewModelScope.launch {
             Log.d("Debug", "Launched coroutine")
            currentUser = firebaseRepository.getCurrentUserData(uid)!!
        }
        return currentUser
    }
}