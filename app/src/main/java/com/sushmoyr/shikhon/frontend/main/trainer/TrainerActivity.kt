package com.sushmoyr.shikhon.frontend.main.trainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.databinding.ActivityTrainerBinding
import com.sushmoyr.shikhon.utils.Constants
import kotlinx.coroutines.launch

class TrainerActivity : AppCompatActivity() {

    private lateinit var navHostFragment : NavHostFragment
    private lateinit var navController : NavController

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseRepository: FirebaseRepository

    private lateinit var binding : ActivityTrainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavigation()

        auth = Firebase.auth
        firebaseRepository = FirebaseRepository
        val currentUser = auth.currentUser

        if(currentUser!=null)
        {
            lifecycleScope.launch {
                val user = firebaseRepository.getCurrentUserData(currentUser.uid)
            }
        }

        val bundle = intent?.getBundleExtra(Constants.USER_INFO_INTENT_BUNDLE_KEY)
        val newUser = bundle?.getParcelable<User>(Constants.USER_INFO_BUNDLE_KEY)
        Log.d("hello", "In Activity: $newUser")

        Log.d("hello", "In Activity: ${resources.getResourceName(navController.currentDestination?.id!!)}")



    }

    private fun setUpBottomNavigation(){

        val accountType = intent.getStringExtra(Constants.ACCOUNT_TYPE)

        navHostFragment  =
            (supportFragmentManager.findFragmentById(R.id.bottom_nav_host) as NavHostFragment?)!!
        navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigationView

        if(accountType == Constants.USER_TYPE_TRAINER.toString())
        {
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu)
        }
        else{
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_menu_trainee)
        }

        bottomNavigationView.setupWithNavController(navController)
    }

}