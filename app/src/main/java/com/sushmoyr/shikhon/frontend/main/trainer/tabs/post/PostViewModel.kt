package com.sushmoyr.shikhon.frontend.main.trainer.tabs.post

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {

    private val repository = FirebaseRepository()

    fun postContent(post: TrainingPost, allImagesSource: ArrayList<Uri>) {
        /**First we will upload all the images
          *Then we will take the image locations
          *Then add those locations to our post
          *Then we will add the post
        **/
        repository.addTrainerPostToDatabase(post, allImagesSource)
    }


}