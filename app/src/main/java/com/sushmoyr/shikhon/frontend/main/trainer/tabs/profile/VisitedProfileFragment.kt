package com.sushmoyr.shikhon.frontend.main.trainer.tabs.profile

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.Review
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.FragmentVisitedProfileBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters.PostListAdapter

class VisitedProfileFragment : Fragment() {

    private var _binding: FragmentVisitedProfileBinding? = null
    private val binding get() = _binding!!

    private val model: ProfileViewModel by activityViewModels()
    private val args: VisitedProfileFragmentArgs by navArgs()

    private val auth = Firebase.auth

    private val postListAdapter: PostListAdapter by lazy {
        PostListAdapter {
            val directions =
                VisitedProfileFragmentDirections.actionVisitedProfileFragmentToPostDetailsFragment(
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
        // Inflate the layout for this fragment
        _binding = FragmentVisitedProfileBinding.inflate(layoutInflater, container, false)

        setUpPostsRecyclerView()
        setUpReviewRecyclerView()


        val uid = args.profileid

        model.userList.observe(viewLifecycleOwner, { users->

            postListAdapter.setUser(users)
            reviewListAdapter.setUser(users)

            users.forEach {
                if(it.uuid == uid){
                    Log.d("User","Found you. updating ui")
                    binding.user = it
                    setUpUi(auth.uid, it.uuid)
                }
            }
        })

        model.allPost.observe(viewLifecycleOwner, { posts->
            val userPosts : MutableList<TrainingPost> = mutableListOf()

            posts.forEach { post->
                if(post.user.uuid == uid){
                    userPosts.add(post)
                }
            }

            postListAdapter.setData(userPosts)
        })

        model.allReviews.observe(viewLifecycleOwner, { reviews->
            val reviewList: MutableList<Review> = mutableListOf()

            reviews.forEach { review->
                if(review.reviewedToUid == uid)
                    reviewList.add(review)
            }

            reviewListAdapter.setData(reviewList)
        })



        binding.aboutSectionExpandButtonVisitedT.setOnClickListener {
            val isExpanded = binding.aboutSectionInfoLayoutVisitedT.visibility

            if (isExpanded == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(binding.aboutSectionVisitedT, AutoTransition())
                //already expanded. collapse
                binding.aboutSectionInfoLayoutVisitedT.visibility = View.GONE
                //set icon resource
                binding.aboutSectionExpandButtonVisitedT.setImageResource(R.drawable.ic_arrow_down)
            } else {
                TransitionManager.beginDelayedTransition(binding.aboutSectionVisitedT, AutoTransition())
                binding.aboutSectionInfoLayoutVisitedT.visibility = View.VISIBLE
                binding.aboutSectionExpandButtonVisitedT.setImageResource(R.drawable.ic_arrow_up)
            }
        }

        binding.rateButton.setOnClickListener {
            val action = VisitedProfileFragmentDirections.actionVisitedProfileFragmentToReviewFragment(uid)
            findNavController().navigate(action)
        }


        return binding.root
    }

    private fun setUpUi(uid: String?, uuid: String) {
        if(uid != uuid){
            binding.buttonsLayout.visibility = View.VISIBLE
        }
        else{
            binding.buttonsLayout.visibility = View.INVISIBLE
        }
    }

    private fun setUpPostsRecyclerView() {
        val postContainer = binding.visitedProfilePosts
        postContainer.adapter = postListAdapter
        postContainer.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpReviewRecyclerView(){
        val reviewContainer = binding.visitedReviewRv
        reviewContainer.adapter = reviewListAdapter
        reviewContainer.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}