package com.sushmoyr.shikhon.frontend.main.trainer.tabs.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.utils.Constants
import kotlinx.coroutines.launch

class EditProfileViewModel: ViewModel() {

    val profileImageUri = MutableLiveData<Any>()
    val coverImageUri = MutableLiveData<Any>()

    val repository = FirebaseRepository()

    fun setProfileImageUri(uri: Any){
        profileImageUri.value = uri
    }

    fun setCoverImageUri(uri: Any){
        coverImageUri.value = uri
    }

    fun updateUser(
        name: String,
        bio: String,
        gender: String,
        birthdate: String,
        contactNo: String,
        coverPhotoUri: Any?,
        profileImageUri: Any?,
        oldUser: User
    ): Task<Void> { //creating new user model

        var newBio = bio
        if(newBio.isEmpty())
            newBio = oldUser.bio
        var newBirthDate = birthdate
        if(birthdate.isEmpty())
            newBirthDate = oldUser.birthdate
        var newContactNo = contactNo
        if(newContactNo.isEmpty())
            newContactNo = oldUser.contactNo

        var newCoverPhotoUri: String
        if(coverPhotoUri is Uri){
            Log.d("User", "Cover photo changed")
            val location = "${Constants.PROFILE_IMAGE_BASE_URL}${oldUser.uuid}/${Constants.COVER_PICTURE_TAIL_URL}"
            Log.d("User", "Upload location: $location")
            uploadImage(coverPhotoUri, location)
            newCoverPhotoUri = location
        }
        else{
            newCoverPhotoUri = oldUser.coverPhotoUri
        }

        var newProfilePicUri: String
        if(profileImageUri is Uri){
            Log.d("User", "Profile photo changed")
            val location = "${Constants.PROFILE_IMAGE_BASE_URL}${oldUser.uuid}/${Constants.PROFILE_PICTURE_TAIL_URL}"
            Log.d("User", "Upload location: $location")
            uploadImage(profileImageUri, location)
            newProfilePicUri = location
        }
        else{
            newProfilePicUri = oldUser.profilePicUri
        }

        val newUser = User(
            oldUser.uuid,
            name,
            oldUser.email,
            oldUser.accountType,
            newProfilePicUri,
            newCoverPhotoUri,
            newBio,
            gender,
            newBirthDate,
            newContactNo
        )

        return repository.updateUser(newUser)
    }

    private fun uploadImage(source: Uri, location: String){
        viewModelScope.launch {
            repository.uploadImage(source, location)
        }
    }
}