package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.databinding.FragmentPostOptionsBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.SharedHomeViewModel

class PostOptionsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPostOptionsBinding? = null
    private val binding get() = _binding!!

    private val model: SharedHomeViewModel by activityViewModels()

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
            model.deletePost()
            findNavController().navigate(R.id.action_postOptionsFragment_to_homeFragment)
        }


        return binding.root
    }
}