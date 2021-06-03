package com.sushmoyr.shikhon.frontend.main.trainer.tabs.search.viewpagertabs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.FragmentPostSearchResultBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters.PostListAdapter
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.search.SearchFragmentDirections
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.search.SearchViewModel


class PostSearchResultFragment : Fragment() {

    private val postListAdapter: PostListAdapter by lazy {
        PostListAdapter{
            val directions = SearchFragmentDirections.actionSearchFragmentToPostDetailsFragment(
                it.postId, it.user.uuid
            )
            findNavController().navigate(directions)
        }
    }

    private val model: SearchViewModel by activityViewModels()

    private var _binding: FragmentPostSearchResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostSearchResultBinding.inflate(inflater, container, false)

        setUpRecyclerView()
        setUpTags()

        model.filteredPosts.observe(viewLifecycleOwner, {data->
            model.selectedTag.observe(viewLifecycleOwner, {tag->
                if (tag == "All"){
                    postListAdapter.setData(data)
                }
                else{
                    val filteredList = mutableListOf<TrainingPost>()

                    data.forEach { post->
                        if(post.tags.contains(tag)){
                            filteredList.add(post)
                            Log.d("Tags", "Added post: ${post.trainingName}")
                        }
                    }

                    postListAdapter.setData(filteredList)
                }
            })
        })

        model.allUsers.observe(viewLifecycleOwner, {userList->
            postListAdapter.setUser(userList)
        })

        return binding.root
    }

    private fun setUpRecyclerView(){
        val recyclerView = binding.postSearchResultRv
        recyclerView.adapter = postListAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpTags(){
        val tags: Array<String> = resources.getStringArray(R.array.tagList)

        for (i in tags.indices){
            val chip = layoutInflater.inflate(R.layout.filter_chip_layout, binding.postSearchTagGroup,false) as Chip
            chip.text = tags[i]
            binding.postSearchTagGroup.addView(chip)
        }

        binding.postSearchTagGroup.setOnCheckedChangeListener { group, checkedId ->
            group.children.forEach {
                if( it is Chip){
                    if (it.id == checkedId){
                        model.setTag(it.text.toString())
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}