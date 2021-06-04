package com.sushmoyr.shikhon.frontend.main.messenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.shikhon.backend.data.ChatInstance
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.databinding.UserListLayoutBinding

class MessengerAdapter(private val currentUserID: String, private val onItemClicked: (ChatInstance) -> Unit):
    RecyclerView.Adapter<MessengerAdapter.MyViewHolder>(){

    private var userList = emptyList<User>()
    private var roomData = emptyList<ChatInstance>()

    inner class MyViewHolder(val binding: UserListLayoutBinding, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun bind(currentItem: ChatInstance){
            val receiverId = when(currentUserID){
                currentItem.chatOwners[0] -> currentItem.chatOwners[1]
                currentItem.chatOwners[1] -> currentItem.chatOwners[0]
                else -> null
            }

            if(receiverId!=null){
                var receiver : User = User()
                userList.forEach {
                    if(it.uuid == receiverId){
                        receiver = it
                    }
                }
                binding.user = receiver
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            UserListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            onItemClicked(roomData[it])
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(roomData.isNotEmpty()){
            holder.bind(roomData[position])
        }
    }

    override fun getItemCount(): Int {
        return roomData.size
    }

    fun setUserData(users: List<User>){
        userList = users
        notifyDataSetChanged()
    }

    fun setRoomData(rooms: List<ChatInstance>){
        roomData = rooms
        notifyDataSetChanged()
    }

}