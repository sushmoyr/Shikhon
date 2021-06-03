package com.sushmoyr.shikhon.frontend.main.trainer.tabs.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository

class SearchViewModel : ViewModel() {

    val repository = FirebaseRepository()

    private val allPosts: MutableLiveData<List<TrainingPost>> = repository.getPostData()
    val allUsers: MutableLiveData<List<User>> = repository.getAllUserData()
    val selectedTag = MutableLiveData<String>()

    val filteredPosts = MutableLiveData<List<TrainingPost>>()
    val filteredUser = MutableLiveData<List<User>>()

    init {
        selectedTag.value = "All"
    }

    fun queryData(query: String){
        searchPostData(query)
        searchUserData(query)
    }

    fun setTag(tag: String){
        selectedTag.value = tag
    }

    private fun searchPostData(query: String) {
        if (query.isEmpty()) {
            filteredPosts.value = emptyList()
        } else {
            val originalPostData = allPosts.value
            val filteredPostData = originalPostData?.filter { trainingPost ->
                (trainingPost.trainingName.contains(
                    query,
                    true
                ) || trainingPost.trainingDescription.contains(
                    query,
                    true
                ) || trainingPost.trainingLocation.contains(query, true))
            }
            filteredPosts.value = filteredPostData
        }
    }

    fun searchUserData(query: String){
        if (query.isEmpty()) {
            filteredUser.value = emptyList()
        }
        else {
            val originalUserData = allUsers.value
            val filteredUserData = originalUserData?.filter { user ->
                (user.name.contains(query, true) || user.bio.contains(query, true))
            }
            filteredUser.value = filteredUserData
        }
    }

}