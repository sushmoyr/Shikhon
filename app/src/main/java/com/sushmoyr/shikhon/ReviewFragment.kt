package com.sushmoyr.shikhon

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.backend.data.Review
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.databinding.FragmentReviewBinding
import java.time.LocalDateTime


class ReviewFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    private val args: ReviewFragmentArgs by navArgs()

    private val auth = Firebase.auth

    private val repo = FirebaseRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)

        binding.submitReviewButton.setOnClickListener {
            val time = getCurrentTime()
            val rating = binding.ratingBar.rating
            val review = binding.reviewText.text.toString()
            val reviewedTo = args.reviewedUserId
            val reviewBy = auth.currentUser?.uid

            val newReview = Review(time, rating, review, reviewedTo, reviewBy!!)
            repo.addReviewToDatabase(newReview)

            Log.d("Review", rating.toString())
            Log.d("Review", args.reviewedUserId)

            this.dismiss()
        }

        return binding.root
    }

    private fun getCurrentTime(): String {
        return LocalDateTime.now().toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}