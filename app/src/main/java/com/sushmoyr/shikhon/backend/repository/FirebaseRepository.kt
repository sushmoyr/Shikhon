package com.sushmoyr.shikhon.backend.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sushmoyr.shikhon.backend.data.*
import com.sushmoyr.shikhon.utils.Constants
import kotlinx.coroutines.tasks.await
import java.util.ArrayList

object FirebaseRepository {

    private val db : FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val storage = Firebase.storage

    private val allPosts = MutableLiveData<List<TrainingPost>>()
    private val allUsers = MutableLiveData<List<User>>()
    private val allReviews = MutableLiveData<List<Review>>()
    private val singlePost = MutableLiveData<TrainingPost>()

    init {
        val refUser = db.collection("users")

        refUser.addSnapshotListener { value, error ->
            if (error != null || value == null) {
                Log.d("exception", "user snapshot failed")
            }
            val userList = value?.toObjects(User::class.java)
            if (userList != null) {
                allUsers.value = userList
            }
        }

        val refReview = db.collection("allReview")
        refReview.addSnapshotListener { value, error ->
            if (error != null || value == null) {
                Log.d("exception", "user snapshot failed")
            }
            val reviewList = value?.toObjects(Review::class.java)
            if (reviewList != null) {
                allReviews.value = reviewList
            }
        }
    }

    suspend fun addUserToDatabase(user: User) {
        db.collection(Constants.USER)
            .document(user.uuid)
            .set(user)
            .addOnSuccessListener {
                Log.d("Debug", "Data added on fireStore")
            }
            .addOnFailureListener {
                Log.d("Debug", "Data add failed")
            }.await()
    }

    fun addReviewToDatabase(review: Review) {
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

    fun updateReactData(postId: String, newReacts: List<String>) {
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

    fun getAllUserData(): MutableLiveData<List<User>> {
        return allUsers
    }

    fun getAllReviews(): MutableLiveData<List<Review>> {
        return allReviews
    }

    fun getSinglePost(postId: String): MutableLiveData<TrainingPost> {
        val ref = db.collection(Constants.POST_BASE_URL).document(postId)

        ref.addSnapshotListener { value, error ->
            if (error != null || value == null) {
                Log.d("SinglePost", "Single Post update failed")
                return@addSnapshotListener
            }
            val post = value.toObject(TrainingPost::class.java)
            singlePost.value = post
        }

        return singlePost
    }

    fun deleteTrainerPost(post: TrainingPost) {
        db.collection(Constants.POST_BASE_URL).document(post.postId).delete()
            .addOnSuccessListener {
                Log.d("DeletePost", "Post with id: ${post.postId} has been deleted")
                if (post.photoUris.isNotEmpty()) {
                    deleteTrainerPostImageData(post.photoLocations)
                }
            }
            .addOnFailureListener {
                Log.d("DeletePost", "Post with id: ${post.postId} Failed to deleted")
            }
    }

    private fun deleteTrainerPostImageData(photoUris: List<String>) {
        for (uri in photoUris) {
            deleteImage(uri)
        }
    }

    private fun deleteImage(uri: String) {
        val storageRef = storage.reference

        try {
            storageRef.child(uri).delete()
                .addOnSuccessListener {
                    Log.d("DeletePost", "Successfully Deleted File $uri")
                }
                .addOnFailureListener {
                    Log.d("DeletePost", "Post delete Failed Nigga nigga")
                }
        } catch (e: Exception) {
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

    //USer Update Region
    fun updateUser(newUser: User): Task<Void> {
        return db.collection(Constants.USER_BASE_URL).document(newUser.uuid)
            .set(newUser)
    }

    fun updateProfileImage(coverPhotoUri: Uri, location: String, pictureType: Int, uid: String){
        val ref = storage.reference.child(location).putFile(coverPhotoUri)
            .addOnSuccessListener {
                //Cover photo upload success. Now get link and update document
                getImageDownloadLink(uid, location, pictureType)
            }
    }

    private fun joinImageLinkToProfile(uid: String, link: Uri?, imageType: Int) {
        val ref = db.collection(Constants.USER_BASE_URL).document(uid)

        when(imageType){
            Constants.PROFILE_PHOTO -> ref.update("profilePicUri", link.toString()).addOnSuccessListener {

                Log.d("updateFix", "Profile photo updated")
            }
            Constants.COVER_PHOTO -> ref.update("coverPhotoUri", link.toString()).addOnSuccessListener {

                Log.d("updateFix", "Cover photo updated")
            }
        }

    }

    //Post Upload Part

    fun addTrainerPostToDatabase(post: TrainingPost, imageSources: ArrayList<Uri>) {
        val ref = db.collection("allPosts").document(post.postId)

        ref.set(post)
            .addOnSuccessListener {
                Log.d("asyncFix", "Post with ${post.postId} added by ${post.user.uuid}")
                //Start uploading images
                uploadPostImages(imageSources, post.photoLocations, post.postId)

            }
            .addOnFailureListener {
                Log.d("Debug", "Something went wrong")
            }

    }

    private fun uploadPostImages(sources: ArrayList<Uri>, destinations: List<String>, postId: String) {

        val storageRef = storage.reference
        for (i in 0 until sources.size) {
            storageRef.child(destinations[i]).putFile(sources[i]).addOnSuccessListener {
                Log.d("asyncFix", "Upload $i successful")
                //get download uri and update document
                getImageDownloadLink(postId, destinations[i], Constants.POST_PHOTOS)
            }
        }
    }

    private fun getImageDownloadLink(id: String, link: String, imageType: Int) {
        val ref = storage.reference.child(link)
        ref.downloadUrl.addOnSuccessListener {
            Log.d("asyncFix", "download url of $imageType = $it")
            if(imageType == Constants.POST_PHOTOS){
                joinImageLinkToPost(id, it)
            }
            else{
                joinImageLinkToProfile(id, it, imageType)
            }
        }
    }

    private fun joinImageLinkToPost(postId: String, uri: Uri?) {
        val ref = db.collection("allPosts").document(postId)
        ref.update("photoUris", FieldValue.arrayUnion(uri.toString())).addOnSuccessListener {
            Log.d("asyncFix", "Photo added to posts")
        }.addOnFailureListener {
            Log.d("asyncFix", "Sorry!!!!!")
        }
    }

    //Messenger part
    const val tags = "messenger"

    fun getUserRooms(ownerId: String): MutableLiveData<List<ChatInstance>>{
        val instances = MutableLiveData<List<ChatInstance>>()

        db.collection("Rooms")
            .whereArrayContains("chatOwners", ownerId)
            .addSnapshotListener{ snapshot, exception->
                if (exception != null || snapshot == null) {
                    Log.d(tags, "Post snapshot failed")
                    return@addSnapshotListener
                }
                val rooms = snapshot.toObjects(ChatInstance::class.java)
                instances.value = rooms
            }

        return instances
    }

    suspend fun getSingleRoom(roomId: String): Task<DocumentSnapshot> {

        return db.collection("Rooms").document(roomId)
            .get()
    }

    fun getMessages(roomId: String): MutableLiveData<List<Message>>{
        val allMessages = MutableLiveData<List<Message>>()

        db.collection("messages/$roomId/chats")
            .orderBy("sendTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception->
                if (exception != null) {
                    Log.d(tags, "Post snapshot failed.")
                    return@addSnapshotListener
                }
                if(snapshot == null || snapshot.isEmpty){
                    allMessages.value = emptyList()
                    Log.d(tags, "Empty")
                }else{
                    val messages = snapshot.toObjects(Message::class.java)
                    messages.forEach{
                        Log.d(tags, it.toString())
                    }
                    allMessages.value = messages
                }
            }

        return allMessages
    }

    fun createNewRoom(roomData: ChatInstance){
        db.collection("Rooms").document(roomData.id).set(roomData)
    }

    fun addNewMessage(roomId: String, updateTime: String, message: Message){
        db.collection("messages/$roomId/chats").add(message)
            .addOnSuccessListener {
                Log.d(tags, "Msg added = ${message.message}")
            }
    }

}