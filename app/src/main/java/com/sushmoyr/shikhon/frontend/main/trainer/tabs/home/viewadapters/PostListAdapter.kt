package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.databinding.TrainerPostLayoutBinding


class PostListAdapter(private val onItemClicked: (TrainingPost) -> Unit) : RecyclerView.Adapter<PostListAdapter.MyViewHolder>() {

    private var postList = emptyList<TrainingPost>()
    var userList = emptyList<User>()
    var tag = "All"

    inner class MyViewHolder(val binding: TrainerPostLayoutBinding, onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }
        fun bind(currentItem: TrainingPost) {

            actualBind(currentItem)
        }

        private fun actualBind(currentItem: TrainingPost){
            binding.post = currentItem
            binding.numberOfReacts.text = currentItem.reacts.size.toString()
            binding.numberOfComments.text = currentItem.comments.size.toString()

            for (user in userList){
                if(user.uuid == currentItem.user.uuid){
                    binding.user = user
                }
            }

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

    fun setUser(userList: List<User>){
        this.userList = userList
        notifyDataSetChanged()
    }

    fun setTagType(tag: String){
        this.tag = tag
        notifyDataSetChanged()
    }

}