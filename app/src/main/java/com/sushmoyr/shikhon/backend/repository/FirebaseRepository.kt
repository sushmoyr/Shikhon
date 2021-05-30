package com.sushmoyr.shikhon.backend.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.utils.Constants
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    private val allPosts = MutableLiveData<List<TrainingPost>>()

    fun addUserToDatabase(user: User) {
        db.collection(Constants.USER)
            .document(user.uuid)
            .set(user)
            .addOnSuccessListener {
                Log.d("Debug", "Data added on fireStore")
            }
            .addOnFailureListener {
                Log.d("Debug", "Data add failed")
            }
    }

    fun addTrainerPostToDatabase(post: TrainingPost, uid: String, postId: String) {
        val ref = db.collection("allPosts").document(postId)

        ref.set(post)
            .addOnSuccessListener {
                Log.d("Debug", "Post with $postId added by $uid")
            }
            .addOnFailureListener {
                Log.d("Debug", "Something went wrong")
            }

    }

    private suspend fun getCurrentUserDataAsync(uid: String): DocumentSnapshot {
        val docRef = db.collection(Constants.USER).document(uid)
        return docRef.get().await()
    }

    suspend fun getCurrentUserData(uid: String): User? {
        Log.d("Debug", "User data called")
        val userData = getCurrentUserDataAsync(uid)

        Log.d("Debug", userData.id)
        val user = userData.toObject<User>()
        if (user != null) {
            Log.d("Debug", user.uuid)
            Log.d("Debug", user.email)
            Log.d("Debug", user.name)
            Log.d("Debug", user.accountType.toString())
            Log.d("Debug", "Uri = ${user.profilePicUri}")
        } else
            Log.d("Debug", "Null User Value")

        return user
    }


    suspend fun getPhotoUrls(photoUris: List<String>): List<String> {
        val uris: MutableList<String> = mutableListOf()
        val storageRef = storage.reference
        for (uri in photoUris) {
            storageRef.child(uri).downloadUrl.addOnSuccessListener {
                if (!uris.contains(it.toString())) {
                    uris.add(it.toString())
                    Log.d("repo", "Got url: $it")
                }
            }.await()
        }
        return uris
    }

    fun updatePost(post: TrainingPost) {
        db.collection(Constants.POST_BASE_URL).document(post.postId)
            .set(post)
            .addOnSuccessListener {
                Log.d("update", "Post with ${post.postId} updated by ${post.user.name}")
            }
            .addOnFailureListener {
                Log.d("update", "Something went wrong while updating")
            }
    }

    fun updateReactData(postId: String , newReacts: List<String>){
        db.collection(Constants.POST_BASE_URL).document(postId)
            .update("reacts", newReacts)
            .addOnSuccessListener {
                Log.d("Reacts", "Added a new React. Total react ${newReacts.size}")
            }
            .addOnFailureListener {
                Log.d("Reacts", "Failed to add react")
            }
    }

    fun getPostData(): MutableLiveData<List<TrainingPost>> {
        val ref = db.collection("allPosts")

        ref.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.d("allPost", "Hoga marche")
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(TrainingPost::class.java)
            allPosts.value = postList
        }

        Log.d("repocheck", "returning from updatePostData()")

        return allPosts
    }

    fun deleteTrainerPost(post: TrainingPost){
        db.collection(Constants.POST_BASE_URL).document(post.postId).delete()
            .addOnSuccessListener {
                Log.d("DeletePost", "Post with id: ${post.postId} has been deleted")
                if(post.photoUris.isNotEmpty())
                {
                    deleteTrainerPostImageData(post.photoUris)
                }
            }
            .addOnFailureListener {
                Log.d("DeletePost", "Post with id: ${post.postId} Failed to deleted")
            }
    }

    private fun deleteTrainerPostImageData(photoUris: List<String>) {
        val storageRef = storage.reference
        for (uri in photoUris){
            storageRef.child(uri).delete().addOnSuccessListener {
                Log.d("DeletePost", "Successfully Deleted File $uri")
            }
                .addOnFailureListener {
                    Log.d("DeletePost", "Post delete Failed Nigga nigga")
                }
        }
    }
}