package com.sushmoyr.shikhon.frontend.auths

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val firebaseRepository = FirebaseRepository
    private val userAccountType = MutableLiveData<Int>()

    fun registerUser(user: User)
    {
        viewModelScope.launch {
            firebaseRepository.addUserToDatabase(user)
        }
    }

    fun setUserAccountType(id: String){
        viewModelScope.launch(Dispatchers.Main) {
            val user = firebaseRepository.getCurrentUserData(id)
            if (user != null) {

                userAccountType.value = user.accountType
                Log.d("update", "user acc type set")
            }
        }
    }
}