package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.backend.data.Comment
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import java.time.LocalDateTime

class DetailsViewModel: ViewModel() {

    private val repository = FirebaseRepository
    var post = MutableLiveData<TrainingPost>()
    //val allPosts = repository.getPostData()
    val imageUriList = MutableLiveData<List<String>>()
    val allUser = repository.getAllUserData()

    val auth = Firebase.auth

    fun getPost(postId: String){
        post = repository.getSinglePost(postId)
    }

    fun setPhotosUris(photoUris: List<String>) {
        imageUriList.value = photoUris
    }
    
    fun addNewComment(comment: String){
        val allComments = post.value!!.comments.toMutableList()
        val newComment = Comment(auth.currentUser!!.uid, getCurrentTime(), comment)
        allComments.add(newComment)
        Log.d("comments", "CommentSize: ${allComments.size}")
        Log.d("comments",newComment.uid)
        Log.d("comments",newComment.time)
        Log.d("comments",newComment.content)
        val postId = post.value!!.postId
        repository.updateCommentsData(postId, allComments)
    }

    private fun getCurrentTime(): String {
        return LocalDateTime.now().toString()
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