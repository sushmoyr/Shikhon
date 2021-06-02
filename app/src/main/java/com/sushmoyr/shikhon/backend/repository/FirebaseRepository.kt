package com.sushmoyr.shikhon.backend.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.sushmoyr.shikhon.backend.data.Comment
import com.sushmoyr.shikhon.backend.data.Review
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.utils.Constants
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    private val allPosts = MutableLiveData<List<TrainingPost>>()
    private val allUsers = MutableLiveData<List<User>>()
    private val allReviews = MutableLiveData<List<Review>>()
    private val singlePost = MutableLiveData<TrainingPost>()

    init {
        val refUser = db.collection("users")

        refUser.addSnapshotListener { value, error ->
            if (error != null || value == null){
                Log.d("exception", "user snapshot failed")
            }
            val userList = value?.toObjects(User::class.java)
            if(userList!=null){
                allUsers.value = userList
            }
        }

        val refReview = db.collection("allReview")
        refReview.addSnapshotListener { value, error ->
            if (error != null || value == null){
                Log.d("exception", "user snapshot failed")
            }
            val reviewList = value?.toObjects(Review::class.java)
            if(reviewList!=null){
                allReviews.value = reviewList
            }
        }
    }

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

    fun addReviewToDatabase(review: Review){
        val ref = db.collection("allReview").document()
            .set(review)
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
                Log.d("Reacts", "React data updated. Total react ${newReacts.size}")
            }
            .addOnFailureListener {
                Log.d("Reacts", "Failed to add react")
            }
    }

    fun getPostData(): MutableLiveData<List<TrainingPost>> {

        val ref = db.collection("allPosts")

        ref.orderBy("postTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.d("exception", "Post snapshot failed")
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(TrainingPost::class.java)
            allPosts.value = postList
        }


        return allPosts
    }

    fun getAllUserData(): MutableLiveData<List<User>>{
        return allUsers
    }

    fun getAllReviews() : MutableLiveData<List<Review>>{
        return allReviews
    }

    fun getSinglePost(postId: String): MutableLiveData<TrainingPost> {
        val ref = db.collection(Constants.POST_BASE_URL).document(postId)

        ref.addSnapshotListener { value, error ->
            if(error!=null || value == null){
                Log.d("SinglePost", "Single Post update failed")
                return@addSnapshotListener
            }
            val post = value.toObject(TrainingPost::class.java)
            singlePost.value = post
        }

        return singlePost
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
            deleteImage(uri)
        }
    }

    private fun deleteImage(uri: String){
        val storageRef = storage.reference

        try {
            storageRef.child(uri).delete()
                .addOnSuccessListener {
                    Log.d("DeletePost", "Successfully Deleted File $uri")
                }
                .addOnFailureListener {
                    Log.d("DeletePost", "Post delete Failed Nigga nigga")
                }
        } catch (e: Exception){
            Log.d("Exception", "Exception in deleteImage. Exception: ${e.message.toString()}")
        }
    }

    fun updateCommentsData(postId: String, allComments: MutableList<Comment>) {
            db.collection(Constants.POST_BASE_URL).document(postId)
                .update("comments", allComments)
                .addOnSuccessListener {
                    Log.d("Comments", "Successfully added comments")
                }
                .addOnFailureListener {
                    Log.d("Comments", "Comments adding failed")
                }
    }

    suspend fun uploadImage(source: Uri, location: String) {
        val storage = FirebaseStorage.getInstance().getReference(location)
        storage.putFile(source).await()
    }

    fun updateUser(newUser: User): Task<Void> {
        return db.collection(Constants.USER_BASE_URL).document(newUser.uuid)
            .set(newUser)
    }
}