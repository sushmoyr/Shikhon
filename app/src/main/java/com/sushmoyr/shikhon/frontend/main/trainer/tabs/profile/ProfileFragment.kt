package com.sushmoyr.shikhon.frontend.main.trainer.tabs.profile

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.Review
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.databinding.FragmentProfileBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters.PostListAdapter

class ProfileFragment : Fragment() {

    val model: ProfileViewModel by activityViewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val auth = Firebase.auth

    private val postListAdapter: PostListAdapter by lazy {
        PostListAdapter {
            val directions =
                ProfileFragmentDirections.actionProfileFragmentToPostDetailsFragment(
                    it.postId,
                    it.user.uuid
                )
            findNavController().navigate(directions)
        }
    }

    private val reviewListAdapter: ReviewListAdapter by lazy {
        ReviewListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val uid = auth.currentUser!!.uid
        var profile: User? = null



        setUpPostsRecyclerView()
        setUpReviewRecyclerView()

        model.allPost.observe(viewLifecycleOwner, { posts->
            val userPosts : MutableList<TrainingPost> = mutableListOf()

            posts.forEach { post->
                if(post.user.uuid == uid){
                    userPosts.add(post)
                }
            }

            postListAdapter.setData(userPosts)
        })

        model.userList.observe(viewLifecycleOwner, { users ->

            postListAdapter.setUser(users)
            reviewListAdapter.setUser(users)

            users.forEach { user ->
                if(user.uuid == uid){
                    //set user to layout
                    binding.user = user
                    profile = user
                }
            }
        })

        model.allReviews.observe(viewLifecycleOwner, { reviews->
            val reviewList: MutableList<Review> = mutableListOf()

            reviews.forEach { review->
                if(review.reviewedToUid == uid)
                    reviewList.add(review)
            }

            reviewListAdapter.setData(reviewList)
        })

        //edit profile
        binding.editProfileButton.setOnClickListener{
            if(profile != null){
                val directions = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(profile!!)
                findNavController().navigate(directions)
            }
            else{
                Toast.makeText(
                    requireContext(),
                    "Profile still loading. Please wait...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        //expand button
        binding.aboutSectionExpandButton.setOnClickListener {
            val isExpanded = binding.aboutSectionInfoLayout.visibility

            if (isExpanded == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(binding.aboutSection, AutoTransition())
                //already expanded. collapse
                binding.aboutSectionInfoLayout.visibility = View.GONE
                //set icon resource
                binding.aboutSectionExpandButton.setImageResource(R.drawable.ic_arrow_down)
            } else {
                TransitionManager.beginDelayedTransition(binding.aboutSection, AutoTransition())
                binding.aboutSectionInfoLayout.visibility = View.VISIBLE
                binding.aboutSectionExpandButton.setImageResource(R.drawable.ic_arrow_up)
            }
        }

        return binding.root
    }

    private fun setUpReviewRecyclerView() {
        val reviewContainer = binding.profileReviewRv
        reviewContainer.adapter = reviewListAdapter
        reviewContainer.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
    }

    private fun setUpPostsRecyclerView() {
        val postContainer = binding.profilePosts
        postContainer.adapter = postListAdapter
        postContainer.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}