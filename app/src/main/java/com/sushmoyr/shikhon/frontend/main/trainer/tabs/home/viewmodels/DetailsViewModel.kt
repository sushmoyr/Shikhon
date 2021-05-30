package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import kotlinx.coroutines.launch

class DetailsViewModel: ViewModel() {

    private val repository = FirebaseRepository()
    var post = MutableLiveData<TrainingPost>()
    val imageUriList = MutableLiveData<List<String>>()

    fun getPost(postId: String){
        post = repository.getSinglePost(postId)
    }

    fun setPhotosUris(photoUris: List<String>) {
        viewModelScope.launch {
            imageUriList.value = repository.getPhotoUrls(photoUris)
        }
    }

    fun cyclePostReact(uid: String, post: TrainingPost): Boolean{
        var cycleValue: Boolean
        val reacts: MutableList<String> = post.reacts.toMutableList()
        if(reacts.contains(uid)){
            Log.d("Reacts", "Removed")
            reacts.remove(uid)
            cycleValue = false
        }
        else{
            Log.d("Reacts", "Added")
            reacts.add(uid)
            cycleValue = true
        }


        for (i in reacts){
            Log.d("Reacts", i)
        }

        repository.updateReactData(post.postId, reacts)
        return cycleValue
    }
}