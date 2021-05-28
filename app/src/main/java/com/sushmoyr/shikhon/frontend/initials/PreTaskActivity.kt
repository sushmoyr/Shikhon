package com.sushmoyr.shikhon.frontend.initials

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.frontend.auths.AuthenticationActivity

class PreTaskActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Debug", "PreTask activity onCreate")

        auth = Firebase.auth

        if (alreadyLoggedIn()) {
            //Already Logged in
            startLoaderActivity()
        } else {
            startAuthActivity()
        }
    }

    private fun startLoaderActivity() {
        val intent = Intent(this, AccountLoaderActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startAuthActivity() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun alreadyLoggedIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }
}