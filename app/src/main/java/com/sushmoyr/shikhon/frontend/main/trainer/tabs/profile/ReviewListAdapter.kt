package com.sushmoyr.shikhon.frontend.main.trainer.tabs.profile

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.shikhon.backend.data.Review
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.databinding.FragmentReviewBinding
import com.sushmoyr.shikhon.databinding.ReviewCardLayoutBinding

class ReviewListAdapter: RecyclerView.Adapter<ReviewListAdapter.MyViewHolder>() {

    private var reviewList = emptyList<Review>()
    private var userList = emptyList<User>()

    inner class MyViewHolder(val binding: ReviewCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(currentItem: Review){
            binding.review = currentItem

            //looking for user
            userList.forEach {
                if(it.uuid == currentItem.reviewByUid)
                    binding.user = it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ReviewCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    override fun getItemCount(): Int {
        Log.d("review", "Size ${reviewList.size}")
        return reviewList.size
    }

    fun setData(reviewList: List<Review>){
        this.reviewList = reviewList
        notifyDataSetChanged()
    }

    fun setUser(userList: List<User>){
        this.userList = userList
        notifyDataSetChanged()
    }
}