package com.sushmoyr.shikhon.frontend.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sushmoyr.shikhon.databinding.FragmentViewPagerBinding
import com.sushmoyr.shikhon.frontend.onboarding.fragments.FirstFragment
import com.sushmoyr.shikhon.frontend.onboarding.fragments.SecondFragment
import com.sushmoyr.shikhon.frontend.onboarding.fragments.ThirdFragment

class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment()
        )

        val adapter =
            ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)

        binding.onboardingViewPager.adapter = adapter

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}