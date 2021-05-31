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
        if(postId!=null){
            viewModel.getPost(postId)
        }

        viewModel.post.observe(viewLifecycleOwner, {value ->
            updateUI(value)
            binding.reactText.text = value.reacts.size.toString()
            //Log.d("BlaBla", "id: "+value.postId)
            Log.d("BlaBla", "title: "+value.trainingName)
            /*Log.d("BlaBla", "Loc: "+value.trainingLocation)
            Log.d("BlaBla", "desc: "+value.trainingDescription)
            Log.d("BlaBla", "time: "+value.postTime)
            Log.d("BlaBla", "photos: "+value.photoUris.toString())*/
            Log.d("BlaBla", "reacts: "+value.reacts.toString())
            Log.d("BlaBla", "comments: "+value.comments.toString())
            Log.d("BlaBla", "uid: "+value.user.uuid)
            Log.d("BlaBla", "email: "+value.user.email)
            Log.d("BlaBla", "profile: "+value.user.profilePicUri)
            Log.d("BlaBla", "Name: "+value.user.name)
            Log.d("BlaBla", "-----------------------------")
        })

        viewModel.imageUriList.observe(viewLifecycleOwner, {
            updateRecyclerView(it)
        })



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