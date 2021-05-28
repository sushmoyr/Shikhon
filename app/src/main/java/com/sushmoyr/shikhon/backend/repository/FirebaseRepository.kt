package com.sushmoyr.shikhon.backend.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.utils.Constants
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val db = Firebase.firestore

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

    fun addTrainerPostToDatabase(post: TrainingPost, uid: String, postId: String){
        db.collection("allPosts").document(postId)
            .set(post)
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

    suspend fun getCurrentUserData(uid: String) : User? {
        Log.d("Debug", "User data called")
        val userData = getCurrentUserDataAsync(uid)

        Log.d("Debug", userData.id)
        val user = userData.toObject<User>()
        if(user != null)
        {
            Log.d("Debug", user.uuid)
            Log.d("Debug", user.email)
            Log.d("Debug", user.name)
            Log.d("Debug", user.accountType.toString())
            Log.d("Debug", "Uri = ${user.profilePicUri}")
        }
        else
            Log.d("Debug", "Null User Value")

        return user

    }

}