package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sushmoyr.shikhon.backend.data.TrainingPost

class SharedHomeViewModel: ViewModel() {

    val post = MutableLiveData<TrainingPost>()
    val test = MutableLiveData<String>()
    val imageDataDetails = MutableLiveData<List<Bitmap>>()
    private val imageList: MutableList<Bitmap> = mutableListOf()
    private var counter: Int = 0

    fun setPostData(item: TrainingPost) {
        post.value = item
    }

    fun selectText(text: String){
        test.value = text + counter++
    }

    private fun setImageDataOfDetails(images: MutableList<Bitmap>) {
        imageDataDetails.value = images
        Log.d("Debug", "ImageDataDetails Size = ${imageDataDetails.value}")
    }

    fun addImageInList(image: Bitmap){
        if(!imageList.contains(image)){
            imageList.add(image)
        }
        setImageDataOfDetails(imageList)
    }
    fun clearImageList(){
        imageList.clear()
    }
}
