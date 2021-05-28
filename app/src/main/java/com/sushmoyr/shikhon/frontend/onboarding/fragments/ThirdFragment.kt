package com.sushmoyr.shikhon.frontend.onboarding.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.databinding.FragmentFirstBinding
import com.sushmoyr.shikhon.databinding.FragmentThirdBinding
import com.sushmoyr.shikhon.utils.Constants

class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)

        binding.finish.setOnClickListener {
            onBoardingFinished()
            findNavController().navigate(R.id.action_viewPagerFragment_to_preTaskActivity)
            activity?.finish()
        }

        return binding.root
    }

    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences(Constants.ONBOARDING_STATE, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(Constants.ONBOARDING_FINISHED, true)
        editor.apply()
    }

}