package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.FragmentPostDetailsBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.SharedHomeViewModel
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters.PostDetailsImageAdapter

class PostDetailsFragment : Fragment() {

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!

    private val model: SharedHomeViewModel by activityViewModels()

    private lateinit var images: MutableList<Bitmap>

    private val adapter : PostDetailsImageAdapter by lazy {
        PostDetailsImageAdapter()
    }
    
    private val auth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        images = mutableListOf()

        //model.clearImageList()

        val post = model.post
        post.observe(viewLifecycleOwner, { data ->
            updateUI(data)
        })

        model.imageUriList.observe(viewLifecycleOwner, {
            updateRecyclerView(it)
        })

        binding.reactButton.setOnClickListener {
            Toast.makeText(requireContext(), "Liked!!!", Toast.LENGTH_SHORT).show()
            post.value?.let { it1 -> model.cyclePostReact(auth.uid.toString(), it1) }
        }

        binding.commentButton.setOnClickListener {
            Toast.makeText(requireContext(), "Comment!!!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun updateRecyclerView(data: List<String>) {
        adapter.setAdapterData(data)
        val recyclerView = binding.detailsPostPhotosRv
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateUI(post: TrainingPost?) {
        if(post != null){
            //update ui data
            binding.post = post

            model.setPhotosUris(post.photoUris)

            if(post.user.uuid == auth.currentUser?.uid){
                binding.moreOptions.visibility = View.VISIBLE
                binding.reactCommentLayout.weightSum = 3F
                binding.moreOptions.setOnClickListener{
                    findNavController().navigate(R.id.action_postDetailsFragment_to_postOptionsFragment)
                }
            }
            else{
                binding.moreOptions.visibility = View.GONE
                binding.reactCommentLayout.weightSum = 2F
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        model.imageUriList.value = emptyList()
    }

}