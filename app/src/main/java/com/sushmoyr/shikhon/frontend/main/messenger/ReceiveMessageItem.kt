package com.sushmoyr.shikhon.frontend.main.messenger

import android.util.Log
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.Message
import com.sushmoyr.shikhon.databinding.ItemMessageRecievedBinding
import com.xwray.groupie.databinding.BindableItem

class ReceiveMessageItem(private val message: Message) : BindableItem<ItemMessageRecievedBinding>() {
    override fun getLayout(): Int {
        return R.layout.item_message_recieved
    }

    override fun bind(viewBinding: ItemMessageRecievedBinding, position: Int) {
        viewBinding.message = message
        Log.d("messenger","Received Added: ${message.message}")
    }
}