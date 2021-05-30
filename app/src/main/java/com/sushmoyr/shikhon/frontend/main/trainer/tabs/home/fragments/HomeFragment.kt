package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.FragmentHomeBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.HomeViewModel
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.SharedHomeViewModel
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters.PostListAdapter

class HomeFragment : Fragment() {
    private val model: SharedHomeViewModel by activityViewModels()
    private val homeModel: HomeViewModel by viewModels()
    private val debug = "Debug"

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var posts : MutableList<TrainingPost>

    private lateinit var firebaseFirestore: FirebaseFirestore

    private val adapter: PostListAdapter by lazy {
        PostListAdapter {//OnClick event
            model.setPostData(it)
            findNavController().navigate(R.id.action_homeFragment_to_postDetailsFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        posts = mutableListOf()

        homeModel.allPost.observe(viewLifecycleOwner, { data->
            adapter.setData(data)
        })


        val recyclerView = binding.newsFeed

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        /*val ref = firebaseFirestore.collection("allPosts")

        ref.addSnapshotListener{ snapshot, exception->
            if(exception != null || snapshot==null ){
                Log.d(debug, "Hoga marche")
                return@addSnapshotListener
            }

            val postList = snapshot.toObjects(TrainingPost::class.java)
            posts.clear().also {
                Log.d(debug, "Cleared posts")
            }
            posts.addAll(postList).also {
                Log.d(debug, "Added post and size = ${posts.size}")
            }
            adapter.setData(posts)
        }*/

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}