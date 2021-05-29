package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.TrainerPostLayoutBinding
import java.io.File


class PostListAdapter(private val onItemClicked: (TrainingPost) -> Unit) : RecyclerView.Adapter<PostListAdapter.MyViewHolder>() {

    private var postList = emptyList<TrainingPost>()

    class MyViewHolder(val binding: TrainerPostLayoutBinding, onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }
        fun bind(currentItem: TrainingPost) {

            binding.postTitle.text = currentItem.trainingName
            binding.postDescription.text = currentItem.trainingDescription
            binding.postLocation.text = currentItem.trainingLocation
            binding.userName.text = currentItem.user.name

            val storageRef = Firebase.storage.reference
            storageRef.child(currentItem.user.profilePicUri).downloadUrl.addOnSuccessListener {
                Log.d("Update", "Profile pic ur success : ${it.toString()}")
                Glide.with(binding.root.context)
                    .load(Uri.parse(it.toString()))
                    .override(binding.profilePic.width, binding.profilePic.height)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(binding.profilePic)
            }

            //setup profile pic
            /*val storageRef = FirebaseStorage.getInstance().reference.child(currentItem.user.profilePicUri)
            Log.d("UserProfile", currentItem.user.profilePicUri)
            val localfile = File.createTempFile("tempProfilePic",".png")
            storageRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)

                Glide.with(binding.root.context)
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
                                Log.d("UserProfile", "profile pic Load failed. Getting Exception")
                                if (e != null) {
                                    Log.d("UserProfile", e.stackTraceToString())
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
                                Log.d("UserProfile", "Profile pic Load Success")
                                return false
                            }

                        }
                    )
                    .into(binding.profilePic)
            }*/

            if (currentItem.photoUris.isNotEmpty()) {
                binding.postPhotos.visibility = View.VISIBLE

                val recyclerView = binding.postPhotos
                val adapter = PostImageListAdapter(currentItem.photoUris)
                val layoutManager =
                    LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = layoutManager
                recyclerView.hasFixedSize()
            }
            else {
                binding.postPhotos.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(TrainerPostLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            onItemClicked(postList[it])
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =
        holder.bind(postList[position])


    override fun getItemCount(): Int {
        Log.d("Debug", "RV size = ${postList.size}")
        return postList.size
    }

    fun setData(postList: List<TrainingPost>) {
        this.postList = postList
        Log.d("Debug", "RV data set")
        notifyDataSetChanged()
    }


}