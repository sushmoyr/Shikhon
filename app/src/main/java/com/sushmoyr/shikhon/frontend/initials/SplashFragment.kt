package com.sushmoyr.shikhon.frontend.initials

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.utils.Constants

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Handler().postDelayed({
            if(firstTimeInstall())
            {
                findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }
            else{
                findNavController().navigate(R.id.action_splashFragment_to_preTaskActivity)
                activity?.finish()
            }
        }, 3000)



        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun firstTimeInstall(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences(Constants.ONBOARDING_STATE, Context.MODE_PRIVATE)
        val value = sharedPref.getBoolean(Constants.ONBOARDING_FINISHED, false)
        Log.d("Debug", value.toString())
        return !value
    }
}