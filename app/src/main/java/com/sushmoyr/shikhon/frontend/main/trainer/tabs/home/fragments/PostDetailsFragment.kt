 package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.FragmentPostDetailsBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters.PostDetailsImageAdapter
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.DetailsViewModel

class PostDetailsFragment : Fragment() {

    private val args: PostDetailsFragmentArgs by navArgs()

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : DetailsViewModel by activityViewModels()

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

        //model.clearImageList()
        val postId = args.detailPostId
        val userId = args.detailPostUserId
        if(postId!=null){
            viewModel.getPost(postId)
        }

        viewModel.post.observe(viewLifecycleOwner, {value ->

            if(value!=null){
                updateUI(value)
                if(!value.reacts.isNullOrEmpty()){
                    binding.reactText.text = value.reacts.size.toString()
                }
                else{
                    binding.reactText.text = "0"
                }
            }

        })

        viewModel.imageUriList.observe(viewLifecycleOwner, {
            updateRecyclerView(it)
        })

        viewModel.allUser.observe(viewLifecycleOwner, { users ->
            for (user in users){
                if(user.uuid == userId){
                    binding.user = user
                }
            }
        })

        binding.detailsUserName.setOnClickListener {
            if (userId == auth.uid){
                findNavController().navigate(R.id.action_global_profileFragment)
            }
            else{
                val directions = PostDetailsFragmentDirections.actionPostDetailsFragmentToVisitedProfileFragment(userId)
                findNavController().navigate(directions)
            }
        }



        binding.commentButton.setOnClickListener {
            Toast.makeText(requireContext(), "Comment!!!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_postDetailsFragment_to_commentsFragment)
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

            viewModel.setPhotosUris(post.photoUris)

            if(post.user.uuid == auth.currentUser?.uid){
                binding.moreOptions.visibility = View.VISIBLE
                binding.reactCommentLayout.weightSum = 3F
                binding.moreOptions.setOnClickListener{
                    val direction = PostDetailsFragmentDirections.actionPostDetailsFragmentToPostOptionsFragment(post)
                    findNavController().navigate(direction)
                }
            }
            else{
                binding.moreOptions.visibility = View.GONE
                binding.reactCommentLayout.weightSum = 2F
            }

            binding.reactButton.setOnClickListener {
                Toast.makeText(requireContext(), "Liked!!!", Toast.LENGTH_SHORT).show()
                viewModel.cyclePostReact(auth.uid!!, post)
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.imageUriList.value = emptyList()
    }

}