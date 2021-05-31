package com.sushmoyr.shikhon.frontend.main.trainer.tabs.post

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.type.DateTime
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.TrainingPost
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.databinding.FragmentPostBinding
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var allImages: ArrayList<Uri>

    private val repository = FirebaseRepository()
    private val currentUser = Firebase.auth.currentUser

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostBinding.inflate(layoutInflater, container, false)

        allImages = ArrayList()

        //getting current user object
        lifecycleScope.launch {
            user = currentUser?.uid?.let { repository.getCurrentUserData(it) }!!
        }

        binding.uploadImage.setOnClickListener {
            uploadImage()
        }

        binding.postButton.setOnClickListener {
            binding.postButton.isClickable = false
            post()
            Toast.makeText(requireContext(), "Uploading Post", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun post() {

        val path = "images/posts"
        val allImageLocation = ArrayList<String>()
        val postTime = getCurrentTime()
        val postId = postTime +"_${user.uuid}"

        for(i in 0 until allImages.count())
        {
            allImageLocation.add("$path/$postId/$i")
        }

        //creating post
        val title = binding.trainingTitleText.text.toString()
        val desc = binding.trainingDescText.text.toString()
        val location = binding.trainingLocationText.text.toString()

        if(verifyPost(title, desc, location))
        {

            val post = TrainingPost(
                postId,
                user,
                title,
                desc,
                location,
                postTime,
                allImageLocation
            )

            postImages(allImageLocation)
            postContent(post)
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.selectedItemId =
                R.id.homeFragment
        }
        else
        {
            Toast.makeText(requireContext(), "Field can't be empty", Toast.LENGTH_SHORT).show()
        }


    }

    private fun postContent(post: TrainingPost) {
        Toast.makeText(requireContext(), "Posting", Toast.LENGTH_SHORT).show()
        FirebaseRepository().addTrainerPostToDatabase(post, currentUser?.uid!!, post.postId)
    }

    private fun verifyPost(title: String, desc: String, location: String): Boolean {
        return when {
            TextUtils.isEmpty(title) -> false
            TextUtils.isEmpty(desc) -> false
            TextUtils.isEmpty(location) -> false
            else -> true
        }
    }

    private fun postImages(allImageLocation: ArrayList<String>) {



        val storage = FirebaseStorage.getInstance()

        for (i in 0 until allImages.count()) {
            val ref = storage.getReference(allImageLocation[i])
            ref.putFile(allImages[i]).addOnSuccessListener {
                Log.d("Debug", "image uploaded")
            }
        }
    }


    private fun uploadImage() {
        val intent = Intent()
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            // if multiple images are selected
            if (data?.clipData != null) {
                var count = data.clipData!!.itemCount

                for (i in 0 until count) {
                    var imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    allImages.add(imageUri)
                }

            } else if (data?.data != null) {
                // if single image is selected

                var imageUri: Uri = data.data!!
                allImages.add(imageUri)
            }
        }

        if (allImages.isNotEmpty()) {
            val adapter = ImageListAdapter(allImages)
            setUpRecyclerView(adapter)
        }
    }

    private fun setUpRecyclerView(adapter: ImageListAdapter) {
        binding.imageGrid.adapter = adapter
        binding.imageGrid.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    private fun getCurrentTime(): String {
        return LocalDateTime.now().toString()
    }
}