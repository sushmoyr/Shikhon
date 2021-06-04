package com.sushmoyr.shikhon.frontend.main.trainer.tabs.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.utils.Constants

class EditProfileViewModel: ViewModel() {

    val profileImageUri = MutableLiveData<Any>()
    val coverImageUri = MutableLiveData<Any>()

    val repository = FirebaseRepository

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



        val newUser = User(
            oldUser.uuid,
            name,
            oldUser.email,
            oldUser.accountType,
            oldUser.profilePicUri,
            oldUser.coverPhotoUri,
            newBio,
            gender,
            newBirthDate,
            newContactNo
        )

        //check if coverPhoto updated
        if (coverPhotoUri is Uri){
            //updated. begin upload task
            val location = "${Constants.PROFILE_IMAGE_BASE_URL}${oldUser.uuid}/${Constants.COVER_PICTURE_TAIL_URL}"
            updateProfileImages(coverPhotoUri, location, Constants.COVER_PHOTO, oldUser.uuid)
        }

        if (profileImageUri is Uri){
            val location = "${Constants.PROFILE_IMAGE_BASE_URL}${oldUser.uuid}/${Constants.PROFILE_PICTURE_TAIL_URL}"
            updateProfileImages(profileImageUri, location, Constants.PROFILE_PHOTO, oldUser.uuid)
        }

        return repository.updateUser(newUser)
    }

    private fun updateProfileImages(coverPhotoUri: Uri, location: String, pictureType: Int, id: String) {
        repository.updateProfileImage(coverPhotoUri, location, pictureType, id)
    }

}