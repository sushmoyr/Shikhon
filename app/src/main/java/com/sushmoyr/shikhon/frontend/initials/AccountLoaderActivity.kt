package com.sushmoyr.shikhon.frontend.initials

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
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.frontend.main.trainer.MainActivity
import com.sushmoyr.shikhon.frontend.main.trainee.TraineeActivity
import com.sushmoyr.shikhon.utils.Constants
import kotlinx.coroutines.launch

class AccountLoaderActivity : AppCompatActivity() {

    private val viewModel: AccountLoaderViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private val firebaseRepository = FirebaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_loader)

        Log.d("Debug", "Loader activity")

        updateCurrentAccountInfo()


    }

    private fun updateCurrentAccountInfo() {
        auth = Firebase.auth
        val currentUser = auth.currentUser


        if (currentUser != null) {
            lifecycleScope.launch {
                val user = firebaseRepository.getCurrentUserData(currentUser.uid)!!
                startTrainerActivity(user.accountType.toString(), user)
            }
        }
        /*if(currentUser != null){
            if(currentUser.displayName == Constants.USER_TYPE_TRAINEE.toString())
                startTraineeActivity()
            else
                startTrainerActivity()
        }*/


    }

    private fun startTraineeActivity() {
        val intent = Intent(this, TraineeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startTrainerActivity(accountType: String, user: User) {
        val bundle = Bundle()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.ACCOUNT_TYPE, accountType)
        bundle.putParcelable(Constants.USER_INFO_BUNDLE_KEY, user)
        intent.putExtra(Constants.USER_INFO_INTENT_BUNDLE_KEY, bundle)
        startActivity(intent)
        finish()
    }
}