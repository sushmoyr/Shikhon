package com.sushmoyr.shikhon.frontend.main.trainee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.frontend.initials.PreTaskActivity

class TraineeActivity : AppCompatActivity() {
    val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = R.layout.activity_trainee
        setContentView(view)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            auth.signOut()

            val intent = Intent(this, PreTaskActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}