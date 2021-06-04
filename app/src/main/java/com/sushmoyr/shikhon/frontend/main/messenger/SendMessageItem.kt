package com.sushmoyr.shikhon.frontend.main.messenger


import android.util.Log
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.Message
import com.sushmoyr.shikhon.databinding.ItemMessageSendBinding
import com.xwray.groupie.databinding.BindableItem


class SendMessageItem (private val message:Message): BindableItem<ItemMessageSendBinding>() {
    override fun bind(binding: ItemMessageSendBinding, position: Int) {
        binding.message = message
        Log.d("messenger","Send Added: ${message.message}")
    }

    override fun getLayout(): Int = R.layout.item_message_send
}