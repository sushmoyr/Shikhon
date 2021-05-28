package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val model: SharedHomeViewModel by activityViewModels()
    private val debug = "Debug"

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var posts : MutableList<TrainingPost>

    private lateinit var firebaseFirestore: FirebaseFirestore

    private val adapter: PostListAdapter by lazy {
        PostListAdapter {
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

        val recyclerView = binding.newsFeed

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        //getting images
        val ref = firebaseFirestore.collection("allPosts")
        val userRef = firebaseFirestore.collection("user")

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
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}