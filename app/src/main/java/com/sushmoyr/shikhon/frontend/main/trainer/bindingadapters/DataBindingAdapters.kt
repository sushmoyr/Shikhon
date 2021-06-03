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
import com.sushmoyr.shikhon.utils.Constants
import java.time.LocalDateTime

class DataBindingAdapters {
    companion object {
        @BindingAdapter("android:sourceUrl")
        @JvmStatic
        fun ImageView.sourceUrl(url: Any?) {

            if (url is String) {
                Glide.with(this)
                    .load(Uri.parse(url))
                    .placeholder(R.drawable.profile_placeholder)
                    .error(R.drawable.profile_placeholder)
                    .override(width, height)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(this)
            }
            else if(url is Uri){
                Glide.with(context)
                    .load(url)
                    .override(width, height)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(this)
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
                    } else {
                        "You and ${reacts.size - 1} $other this"
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

        @BindingAdapter("android:parseDateTime")
        @JvmStatic
        fun TextView.parseDateTime(dateTime: String?) {
            if (dateTime != null) {
                val id = this.id
                val name = resources.getResourceName(id)
                Log.d("error", "we are at ${name},")
                val updateDateTime = LocalDateTime.parse(dateTime)
                val finalDateStamp =
                    "${updateDateTime.dayOfMonth} ${updateDateTime.month.name}, ${updateDateTime.year}"
                val finalTimeStamp = "${updateDateTime.hour}:${updateDateTime.minute}"
                val finalText = "$finalDateStamp at $finalTimeStamp"
                Log.d("finalTimeText", finalText)
                text = finalText
            }
        }

        @BindingAdapter("android:setAccType")
        @JvmStatic
        fun TextView.setAccType(type: Int?) {
            if(type != null){
                var accType: String = ""
                if(type == Constants.USER_TYPE_TRAINER){
                    accType = "Trainer"
                }
                else {
                    accType = "Student"
                }
                text = accType
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