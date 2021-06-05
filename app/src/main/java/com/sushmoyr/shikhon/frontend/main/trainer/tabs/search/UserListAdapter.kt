package com.sushmoyr.shikhon.frontend.main.trainer.tabs.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.databinding.UserListLayoutBinding

class UserListAdapter(private val onItemClicked: (String) -> Unit) :
    RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()

    inner class MyViewHolder(val binding: UserListLayoutBinding, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                binding.root.setOnClickListener {
                    onItemClicked(adapterPosition)
                }

            }

            fun bind(currentItem: User){
                binding.user = currentItem
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            UserListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            onItemClicked(userList[it].uuid)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(users: List<User>){
        userList = users
        notifyDataSetChanged()
    }
}