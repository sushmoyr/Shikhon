package com.sushmoyr.shikhon.frontend.initials

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.frontend.main.trainer.TrainerActivity
import com.sushmoyr.shikhon.frontend.main.trainee.TraineeActivity
import com.sushmoyr.shikhon.utils.Constants
import kotlinx.coroutines.launch

class AccountLoaderActivity : AppCompatActivity() {

    private val viewModel: AccountLoaderViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private val firebaseRepository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_loader)

        Log.d("Debug", "Loader activity")

        updateCurrentAccountInfo()


    }

    private fun updateCurrentAccountInfo() {

        val sharedPref =
            this.getSharedPreferences(Constants.ACCOUNT_TYPE_EDITOR_KEY, Context.MODE_PRIVATE)
        val value = sharedPref.getInt(Constants.ACCOUNT_TYPE_KEY, -1)
        Log.d("update", "loader = $value")

        when(value){
            0 -> startTrainerActivity()
            1 -> startTraineeActivity()
            else -> finish()
        }

        /*auth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser != null) {
            lifecycleScope.launch {
                val user = firebaseRepository.getCurrentUserData(currentUser.uid)!!

                if (user.accountType == 0) {
                    startTrainerActivity()
                } else {
                    startTraineeActivity()

                }
            }
        }*/
    }

    private fun startTraineeActivity() {
        val intent = Intent(this, TraineeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startTrainerActivity() {
        val intent = Intent(this, TrainerActivity::class.java)
        startActivity(intent)
        finish()
    }
}