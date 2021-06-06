package com.sushmoyr.shikhon.frontend.main.trainer.tabs.bookmarks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sushmoyr.shikhon.backend.data.Bookmark
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository

class BookmarksViewModel : ViewModel() {

    private val repo = FirebaseRepository

    val allPosts = repo.getPostData()
    private val bookmarkedPostIds = MutableLiveData<List<Bookmark>>()
    val allUsers = repo.getAllUserData()

    fun getBookmarkedPostsId(uid: String): MutableLiveData<List<Bookmark>> {

        return repo.getAllBookmarks(uid)
    }
}