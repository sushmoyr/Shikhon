package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.databinding.FragmentPostOptionsBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.SharedHomeViewModel

class PostOptionsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPostOptionsBinding? = null
    private val binding get() = _binding!!

    private val args: PostOptionsFragmentArgs by navArgs()

    private val model: SharedHomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostOptionsBinding.inflate(layoutInflater, container, false)

        val post = args.post

        binding.editPostView.setOnClickListener {
            Toast.makeText(requireContext(), "Editing", Toast.LENGTH_SHORT).show()
            val action = PostOptionsFragmentDirections.actionPostOptionsFragmentToUpdatePostFragment(post)
            findNavController().navigate(action)
        }

        binding.deletPostView.setOnClickListener {
            Toast.makeText(requireContext(), "Deleting", Toast.LENGTH_SHORT).show()
            model.deletePost(post)
            findNavController().navigate(R.id.action_postOptionsFragment_to_homeFragment)
        }


        return binding.root
    }
}