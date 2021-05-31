package com.sushmoyr.shikhon

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sushmoyr.shikhon.backend.data.Comment
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.FragmentCommentsBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters.CommentsAdapter
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewmodels.DetailsViewModel

class CommentsFragment : Fragment() {

    private val model: DetailsViewModel by activityViewModels()

    private val adapter: CommentsAdapter by lazy {
        CommentsAdapter()
    }

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentsBinding.inflate(layoutInflater, container, false)

        setUpRecyclerView()

        model.post.observe(viewLifecycleOwner, { data->
            addCommentsToView(data)
        })

        binding.addCommentButton.setOnClickListener{
            val text = binding.comment.text.toString()
            binding.comment.text?.clear()
            if(text.isEmpty()){
                Toast.makeText(requireContext(), "No comments written", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            addNewComment(text)
        }

        return binding.root
    }

    private fun addNewComment(text: String) {
        Log.d("Comments", text.toString())
        model.addNewComment(text)
    }

    private fun addCommentsToView(post: TrainingPost?) {
        if(post != null){
            val comments = post.comments
            if(!comments.isNullOrEmpty()){
                updateRecyclerData(comments)
            } else{
                Log.d("Comments", "No comments")
            }
        }
    }

    private fun setUpRecyclerView(){
        binding.commentsRv.adapter = adapter
        binding.commentsRv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateRecyclerData(comments: List<Comment>) {
        adapter.setData(comments)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}