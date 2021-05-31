package com.sushmoyr.shikhon.frontend.main.trainer.bindingadapters

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.ktx.auth
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

            if (url != null) {
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
            if (dateId != null) {
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

        @BindingAdapter("android:setReactColor")
        @JvmStatic
        fun setReactColor(view: View, reacts: List<String>?) {
            if (view is ImageView) {
                if (isLiked(reacts)) {
                    val color = ContextCompat.getColor(view.context, R.color.blue_500)
                    view.setColorFilter(color)
                } else {
                    val color = ContextCompat.getColor(
                        view.context,
                        R.color.material_on_background_disabled
                    )
                    view.setColorFilter(color)
                }
            }

            if (view is TextView) {
                if (isLiked(reacts)) {
                    val color = ContextCompat.getColor(view.context, R.color.blue_500)
                    view.setTextColor(color)
                } else {
                    val color = ContextCompat.getColor(
                        view.context,
                        R.color.material_on_background_disabled
                    )
                    view.setTextColor(color)
                }
            }

        }

        @SuppressLint("SetTextI18n")
        @BindingAdapter("android:setReactionText")
        @JvmStatic
        fun TextView.setReactionText(reacts: List<String>?) {
            when {
                reacts.isNullOrEmpty() -> {
                    text = resources.getString(R.string.no_like_text)
                }
                isLiked(reacts) -> {
                    val other = if (reacts.size == 2) {
                        "other person likes"
                    } else {
                        "people like"
                    }
                    text = if (reacts.size == 1) {
                        "You liked this post"
                    }
                    else{
                        "You and ${reacts.size-1} $other this"
                    }

                }
                else -> {
                    var other = if (reacts.size == 1) {
                        "person likes"
                    } else {
                        "people like"
                    }
                    text = "${reacts.size} $other this"
                }
            }
        }

        private fun isLiked(reacts: List<String>?): Boolean {
            val uid = Firebase.auth.uid
            return if (!reacts.isNullOrEmpty() && uid != null && reacts.contains(uid)) {
                Log.d("Reacts", "Already liked")
                true
            } else {
                false
            }
        }
    }
}