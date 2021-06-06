package com.sushmoyr.shikhon.frontend.main.trainer.tabs.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.backend.data.Bookmark
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.FragmentBookmarkBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters.PostListAdapter

class BookmarkFragment : Fragment() {

    private val auth = Firebase.auth

    private val adapter: PostListAdapter by lazy {
        PostListAdapter {
            val directions = BookmarkFragmentDirections.actionBookmarkFragmentToPostDetailsFragment(
                it.postId,
                it.user.uuid
            )
            findNavController().navigate(directions)
        }
    }

    private val model: BookmarksViewModel by viewModels()


    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)

        setUpRecyclerView()

        val currentUser = auth.currentUser

        if(currentUser!= null){

            val allBookmarksId = model.getBookmarkedPostsId(currentUser.uid)
            allBookmarksId.observe(viewLifecycleOwner, { bookmarks ->

                model.allPosts.observe(viewLifecycleOwner, {posts->

                    val filteredPosts = mutableListOf<TrainingPost>()

                    posts.forEach {
                        val bm = Bookmark(it.postId)
                        if(bookmarks.contains(bm))
                            filteredPosts.add(it)
                    }

                    adapter.setData(filteredPosts)
                })

            })


        }

        model.allUsers.observe(viewLifecycleOwner, {userData ->
            adapter.setUser(userData)
        })


        return binding.root
    }

    private fun setUpRecyclerView() {
        val rv = binding.bookmarkedPosts
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}