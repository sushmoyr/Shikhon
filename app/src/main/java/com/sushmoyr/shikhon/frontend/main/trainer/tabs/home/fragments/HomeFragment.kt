package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.fragments

import android.os.Bundle
import android.util.Log
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
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.databinding.FragmentHomeBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.HomeViewModel
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.SharedHomeViewModel
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters.PostListAdapter

class HomeFragment : Fragment() {
    private val homeModel: HomeViewModel by viewModels()
    private val debug = "Debug"

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var firebaseFirestore: FirebaseFirestore

    private val adapter: PostListAdapter by lazy {
        PostListAdapter {//OnClick event
            val directions = HomeFragmentDirections.actionHomeFragmentToPostDetailsFragment(it.postId, it.user.uuid)
            findNavController().navigate(directions)
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

        homeModel.allPost.observe(viewLifecycleOwner, { data->
            adapter.setData(data)
            Log.d("realtime", "post data changed")
        })

        homeModel.allUsers.observe(viewLifecycleOwner, { users ->
            adapter.setUser(users)
            Log.d("realtime","User data changed")
        })


        val recyclerView = binding.newsFeed

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}