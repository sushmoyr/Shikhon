package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.FragmentUpdatePostBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.SharedHomeViewModel
import com.sushmoyr.shikhon.utils.Verify

class UpdatePostFragment : Fragment() {

    private var _binding: FragmentUpdatePostBinding? = null
    private val binding get() = _binding!!

    private val model: SharedHomeViewModel by activityViewModels()

    private val args: UpdatePostFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdatePostBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this

        val post = args.post

        updateUI(post)

        binding.updatePostButton.setOnClickListener {
            binding.invalidateAll()
            val newPost = getNewPost()
            binding.post = newPost
            updatePost(newPost)
        }

        binding.cancelUpdateButton.setOnClickListener {
            findNavController().navigateUp()
        }

        Verify.listenToText(binding.trainingTitleUpdateText, "Field can't be empty")

        return binding.root
    }


    private fun getNewPost(): TrainingPost {
        val post = binding.post!!

        val title = binding.trainingTitleUpdateText.text.toString()
        val content = binding.trainingDescText.text.toString()
        val location = binding.updateLocationText.text.toString()

        return TrainingPost(
            post.postId,
            user = post.user,
            trainingName = title,
            trainingDescription = content,
            trainingLocation = location,
            photoUris = post.photoUris,
            photoLocations = post.photoLocations
        )
    }

    private fun updatePost(newPost: TrainingPost?) {
        if (newPost != null) {
            Log.d("update", "====== Post To Update =========")
            Log.d("update", "Title: ${newPost.trainingName}")
            Log.d("update", "Description: ${newPost.trainingDescription}")
            Log.d("update", "Location: ${newPost.trainingLocation}")
            Log.d("update", "===============================")
            model.updatePost(newPost)
            Toast.makeText(requireContext(), "Your post is being updated", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updatePostFragment_to_postDetailsFragment)
        }
    }

    private fun updateUI(post: TrainingPost?) {
        if(post != null){
            binding.post = post
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}