package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.shikhon.backend.data.Comment
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.databinding.CommentsLayoutBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.DetailsViewModel

class CommentsAdapter() : RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {

    private var comments = emptyList<Comment>()
    private var users = emptyList<User>()

    inner class MyViewHolder(val binding: CommentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: Comment) {
            /*binding.lifecycleOwner?.let { lifecycleOwner ->
                model.allUser.observe(lifecycleOwner, {
                allUser.addAll(it)
            }) }

            */
            binding.comment = currentItem
            val userId = currentItem.uid

            var commentUser : User? = null

            for (user in users){
                if(user.uuid == userId){
                    commentUser = user
                }
            }

            if(commentUser != null){
                binding.user = commentUser
            }
            else{
                binding.root.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CommentsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =
        holder.bind(comments[position])

    override fun getItemCount(): Int {
        Log.d("Comments", comments.size.toString())
        return comments.size
    }
    fun setData(comments: List<Comment>) {
        this.comments = comments
        Log.d("comments", "Comments set data")
        notifyDataSetChanged()
    }

    fun setUserData(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }


}