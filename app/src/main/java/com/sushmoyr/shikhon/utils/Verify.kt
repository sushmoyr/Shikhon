package com.sushmoyr.shikhon.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
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


    //User Input Verifications

    fun checkCurrentPassword(currentPass: EditText){
        currentPass.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if(count==0){
                    currentPass.error = "Required Field!! Can't be empty"
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if(s.length !in 33 downTo 6){
                        Log.d("text listener", "On error: $s")
                        currentPass.error = "Password length must be from 6 to 32 characters"
                    }else{
                        currentPass.error = null
                        Log.d("text listener", "On clear error: $s")
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    fun matchPassword(confirmPass: EditText, currentPass: String){
        confirmPass.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if(s.isNotEmpty() && s != currentPass)
                        confirmPass.error = "Password didn't match"
                    else
                        confirmPass.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

}