package com.sushmoyr.shikhon.frontend.initials

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sushmoyr.shikhon.R
import java.time.LocalDateTime

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val date = LocalDateTime.now()
        Log.d("Splash", date.toString())
    }
}