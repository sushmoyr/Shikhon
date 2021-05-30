package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import kotlinx.coroutines.launch

class SharedHomeViewModel: ViewModel() {

    private val firebaseRepo = FirebaseRepository()
    val post = MutableLiveData<TrainingPost>()
    val test = MutableLiveData<String>()
    //private val imageList: MutableList<Bitmap> = mutableListOf()




    fun updatePost(post: TrainingPost){
        firebaseRepo.updatePost(post)
    }

    fun setPostData(item: TrainingPost) {
        post.value = item
    }




    /*fun clearImageList(){
        imageList.clear()
    }*/

    fun deletePost(){
        val post = post.value
        if (post != null) {
            firebaseRepo.deleteTrainerPost(post)
        }
    }
}
