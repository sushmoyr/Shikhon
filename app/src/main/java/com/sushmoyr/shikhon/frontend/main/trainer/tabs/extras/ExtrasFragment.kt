package com.sushmoyr.shikhon.frontend.main.trainer.tabs.extras

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.gms.tasks.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.databinding.FragmentExtrasBinding
import com.sushmoyr.shikhon.frontend.initials.PreTaskActivity
import com.sushmoyr.shikhon.utils.Constants
import java.util.*

class ExtrasFragment : Fragment() {

    private var _binding: FragmentExtrasBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExtrasBinding.inflate(layoutInflater, container, false)
        val bundle = requireActivity().intent.getBundleExtra(Constants.USER_INFO_INTENT_BUNDLE_KEY)
        val user = bundle?.getParcelable<User>(Constants.USER_INFO_BUNDLE_KEY)

        if(user!=null){
            if(user.accountType == Constants.USER_TYPE_TRAINER){
                binding.trainerIdRequestButtonExtras.visibility = View.GONE
                binding.rootLinearLayout.weightSum = 4f
            }
            else{
                binding.trainerIdRequestButtonExtras.visibility = View.VISIBLE
                binding.rootLinearLayout.weightSum = 5f
            }
        }

        Log.d("Bundle", user.toString())
        binding.signOutButtonExtras.setOnClickListener {
            signOut()
        }

        binding.messengerButtonExtras.setOnClickListener {
            findNavController().navigate(R.id.action_extrasFragment_to_messengerFragment)
        }

        binding.collectionButtonExtras.setOnClickListener {
            findNavController().navigate(R.id.action_extrasFragment_to_bookmarkFragment)
        }

        binding.trainerIdRequestButtonExtras.setOnClickListener {
            Toast.makeText(requireContext(), "Opening Browser", Toast.LENGTH_SHORT).show()
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/DKa7tP3BfKqeQaRn9"))
            startActivity(browserIntent)
        }

        return binding.root
    }

    private fun signOut(){
        if (auth.currentUser != null) {
            auth.signOut()
            val intent = Intent(activity, PreTaskActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }
    }
}