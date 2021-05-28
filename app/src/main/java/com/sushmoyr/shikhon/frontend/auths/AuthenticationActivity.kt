package com.sushmoyr.shikhon.frontend.auths

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sushmoyr.shikhon.R

class AuthenticationActivity : AppCompatActivity() {
    private val navHostFragment  =
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
    val navController = navHostFragment?.navController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication_activvity)
    }
}