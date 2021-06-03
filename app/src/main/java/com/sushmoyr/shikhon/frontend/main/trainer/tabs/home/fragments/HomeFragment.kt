package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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
        homeModel.setTag("All")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)



        homeModel.allPost.observe(viewLifecycleOwner, { data->
            homeModel.selectedTag.observe(viewLifecycleOwner, {tag->
                if(tag == "All"){
                    adapter.setData(data)
                }
                else{
                    val filteredList = mutableListOf<TrainingPost>()

                    data.forEach { post->
                        if(post.tags.contains(tag)){
                            filteredList.add(post)
                            Log.d("Tags", "Added post: ${post.trainingName}")
                        }
                    }

                    adapter.setData(filteredList)
                }
            })
        })

        homeModel.allUsers.observe(viewLifecycleOwner, { users ->
            adapter.setUser(users)
            Log.d("realtime","User data changed")
        })


        val recyclerView = binding.newsFeed

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tags: Array<String> = resources.getStringArray(R.array.tagList)

        for(i in tags.indices){
            val chip = layoutInflater.inflate(R.layout.filter_chip_layout, binding.filterType, false) as Chip
            chip.text = tags[i]
            binding.filterType.addView(chip)
        }

        binding.filterType.setOnCheckedChangeListener { group, checkedId ->
            group.children.forEach {
                if(it is Chip){
                    if (it.id == checkedId){
                        Log.d("tags", "Selected: ${it.text}")
                        homeModel.setTag(it.text.toString())
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}