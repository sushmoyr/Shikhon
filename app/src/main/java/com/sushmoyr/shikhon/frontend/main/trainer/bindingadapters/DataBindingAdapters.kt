package com.sushmoyr.shikhon.frontend.main.trainer.bindingadapters

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sushmoyr.shikhon.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataBindingAdapters {
    companion object {
        @BindingAdapter("android:sourceUrl")
        @JvmStatic
        fun ImageView.sourceUrl(url: String?) {

            if(url!=null){
                val storageRef = Firebase.storage.reference
            storageRef.child(url).downloadUrl.addOnSuccessListener {
                Log.d("bindingAdapter", "Profile pic ur success : $it")
                Glide.with(this)
                    .load(Uri.parse(it.toString()))
                    .override(width, height)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(this)
            }
            }
        }

        @BindingAdapter("android:parseDate")
        @JvmStatic
        fun TextView.parseDate(dateId: String?) {
            if(dateId!=null){
                val pattern = "yyyy_MM_dd_hh_mm_ss"
                val dateText = dateId.substring(0, pattern.length)
                Log.d("bindingAdapter", dateText)
                val formatter = DateTimeFormatter.ofPattern(pattern)
                val date = LocalDate.parse(dateText, formatter)
                val time = "${dateText.substring(11, 13)}:${dateText.substring(14, 16)}"
                val finalTimeStamp = "${date.dayOfMonth} ${date.month.name}, ${date.year} at $time"
                Log.d("bindingAdapter", "Time = $finalTimeStamp")
                text = finalTimeStamp
            }
        }

        @BindingAdapter("android:setReactColor", "android:userId", requireAll = true)
        @JvmStatic
        fun ImageView.setReactColor(reacts: List<String>, uid: String) {
            if(reacts.contains(uid)){
                //change color to red
                val color = ContextCompat.getColor(context, R.color.blue_500)
                this.setColorFilter(color)
                Log.d("Reacts", "Already liked")
            }
            else{
                //change color to grey
                val color = ContextCompat.getColor(context, R.color.material_on_background_disabled)
                this.setColorFilter(color)
            }

        }
    }
}