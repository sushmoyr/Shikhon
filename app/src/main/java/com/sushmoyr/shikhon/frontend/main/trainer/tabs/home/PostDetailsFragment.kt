package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.databinding.FragmentPostDetailsBinding
import java.io.File

class PostDetailsFragment : Fragment() {

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!

    private val model: SharedHomeViewModel by activityViewModels()

    private lateinit var images: MutableList<Bitmap>

    private val adapter : PostDetailsImageAdapter by lazy {
        PostDetailsImageAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)

        images = mutableListOf()

        model.clearImageList()

        model.post.observe(viewLifecycleOwner, { post ->
            updateUI(post)
        })

        model.imageDataDetails.observe(viewLifecycleOwner, {data->
            Log.d("Debug", "ViewModel: update called")
            updateRecyclerView(data)
        })

        return binding.root
    }

    private fun updateRecyclerView(data: List<Bitmap>) {
        adapter.setAdapterData(data)
        val recyclerView = binding.detailsPostPhotosRv
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateUI(post: TrainingPost?) {
        if(post != null){
            //update ui data
            binding.post = post
            fetchImageData(post.photoUris)
        }
    }


    private fun fetchImageData(photoUris: List<String>) {
        val images: MutableList<Bitmap> = mutableListOf()
        val storageInstance = FirebaseStorage.getInstance()
        for(uri in photoUris){
            val storageRef = storageInstance.reference.child(uri)
            val localFile = File.createTempFile("tempImage", "jpg")
            storageRef.getFile(localFile).addOnSuccessListener{
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                Log.d("Debug", "Added an image in details fragment")
                model.addImageInList(bitmap)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}