package com.sushmoyr.shikhon.frontend.onboarding.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.databinding.FragmentFirstBinding
import com.sushmoyr.shikhon.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardingViewPager)

        binding.arrow.setOnClickListener {
            viewPager?.currentItem = 2
        }

        return binding.root
    }
}