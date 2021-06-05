package com.sushmoyr.shikhon.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import org.w3c.dom.Text
import java.time.LocalDateTime

object Verify {

    fun listenToText(view: EditText, error: String) {
        view.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if(s.isNullOrEmpty()){
                    view.error = error
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()){
                    view.error = error
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    fun createIdForChatRoom(user1: String, user2: String): String {
        return when{
            user1>user2 -> user1+user2
            else -> user2+user1
        }
    }

    fun getCurrentTime(): String {
        return LocalDateTime.now().toString()
    }

}