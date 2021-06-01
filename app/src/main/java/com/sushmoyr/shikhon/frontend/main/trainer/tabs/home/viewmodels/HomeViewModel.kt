package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository

class HomeViewModel : ViewModel() {

    private val repo : FirebaseRepository = FirebaseRepository()
    val allPost : LiveData<List<TrainingPost>> = repo.getPostData()
    val allUsers : LiveData<List<User>> = repo.getAllUserData()

}