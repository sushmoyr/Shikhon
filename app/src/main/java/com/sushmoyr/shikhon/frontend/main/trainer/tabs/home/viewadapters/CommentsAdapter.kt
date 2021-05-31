package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters

import android.util.Log
import android.view.LayoutInflater
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

    class MyViewHolder(val binding: CommentsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: Comment) {
            /*binding.lifecycleOwner?.let { lifecycleOwner ->
                model.allUser.observe(lifecycleOwner, {
                allUser.addAll(it)
            }) }

            */
            binding.commentTime.text = currentItem.time
            binding.content.text = currentItem.content
            binding.userNameCm.text = currentItem.uid

            Log.d("comments", binding.commentTime.text.toString())
            Log.d("comments", binding.content.text.toString())
            Log.d("comments", binding.userNameCm.text.toString())
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
}