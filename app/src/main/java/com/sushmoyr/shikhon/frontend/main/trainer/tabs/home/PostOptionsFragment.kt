package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.databinding.FragmentPostOptionsBinding

class PostOptionsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPostOptionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostOptionsBinding.inflate(layoutInflater, container, false)

        binding.editPostView.setOnClickListener {
            Toast.makeText(requireContext(), "Editing", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_postOptionsFragment_to_updatePostFragment)
        }

        binding.deletPostView.setOnClickListener {
            Toast.makeText(requireContext(), "Deleting", Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }
}