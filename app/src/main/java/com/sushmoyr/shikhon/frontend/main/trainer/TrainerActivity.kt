package com.sushmoyr.shikhon.frontend.main.trainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.databinding.ActivityTrainerBinding
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
                firebaseRepository.getCurrentUserData(currentUser.uid)
            }
        }

        Log.d("hello", "In Activity: ${resources.getResourceName(navController.currentDestination?.id!!)}")



    }

    private fun setUpBottomNavigation(){
        navHostFragment  =
            (supportFragmentManager.findFragmentById(R.id.bottom_nav_host) as NavHostFragment?)!!
        navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigationView

        bottomNavigationView.setupWithNavController(navController)
    }

}