package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.sushmoyr.shikhon.databinding.ImageListBinding
import java.io.File

class PostImageListAdapter(private val newList: List<String>) :
    RecyclerView.Adapter<PostImageListAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ImageListBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ImageListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newList[position]
        /*val storageRef = FirebaseStorage.getInstance().reference.child(currentItem)
        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

            Glide.with(holder.itemView.context)
                .load(bitmap)
                .centerCrop()
                .listener(
                    object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d("Debug", "Load failed. Getting Exception")
                            if (e != null) {
                                Log.d("Debug", e.stackTraceToString())
                            }
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d("Debug", "Load Success")
                            return false
                        }

                    }
                )
                .into(holder.binding.image)
        }*/

        val storageRef = Firebase.storage.reference
        storageRef.child(currentItem).downloadUrl.addOnSuccessListener {
            Log.d("Update", "Post image load success : ${it.toString()}")
            Glide.with(holder.itemView.context)
                .load(Uri.parse(it.toString()))
                .listener(
                    object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d("Debug", "Load failed on details. Getting Exception")
                            if (e != null) {
                                Log.d("Debug", e.stackTraceToString())
                            }
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d("Debug", "Load Success on details")
                            holder.binding.loadingImage.visibility = View.GONE
                            return false
                        }

                    }
                )
                .override(holder.binding.image.width, holder.binding.image.height)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.binding.image)
        }
    }

    override fun getItemCount(): Int {
        return newList.size
    }

}