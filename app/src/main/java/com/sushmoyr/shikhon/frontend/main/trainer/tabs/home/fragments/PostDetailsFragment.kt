package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.FragmentPostDetailsBinding
import com.sushmoyr.shikhon.frontend.main.trainer.bindingadapters.DataBindingAdapters.Companion.setReactColor
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.SharedHomeViewModel
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters.PostDetailsImageAdapter
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.DetailsViewModel

class PostDetailsFragment : Fragment() {

    private val args: PostDetailsFragmentArgs by navArgs()

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : DetailsViewModel by viewModels()

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
        val postId = args.detailPostId
        if(postId!=null){
            viewModel.getPost(postId)
        }

        viewModel.post.observe(viewLifecycleOwner, {value ->
            updateUI(value)
            Log.d("BlaBla", value.postId)
            Log.d("BlaBla", value.trainingName)
            Log.d("BlaBla", value.trainingLocation)
            Log.d("BlaBla", value.trainingDescription)
            Log.d("BlaBla", value.postTime)
            Log.d("BlaBla", value.photoUris.toString())
            Log.d("BlaBla", value.reacts.toString())
            Log.d("BlaBla", value.comments.toString())
            Log.d("BlaBla", value.user.uuid)
            Log.d("BlaBla", value.user.email)
            Log.d("BlaBla", value.user.profilePicUri)
            Log.d("BlaBla", value.user.name)
            Log.d("BlaBla", value.user.profilePicUri)
        })

        viewModel.imageUriList.observe(viewLifecycleOwner, {
            updateRecyclerView(it)
        })

        binding.reactButton.setOnClickListener {
            Toast.makeText(requireContext(), "Liked!!!", Toast.LENGTH_SHORT).show()
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

            viewModel.setPhotosUris(post.photoUris)

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
        viewModel.imageUriList.value = emptyList()
    }

}