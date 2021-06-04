package com.sushmoyr.shikhon.frontend.main.trainer.tabs.extras

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.gms.tasks.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.databinding.FragmentExtrasBinding
import com.sushmoyr.shikhon.frontend.initials.PreTaskActivity
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

        binding.logoutButton.setOnClickListener {
            val data: MutableMap<String, Any> = HashMap()
            data["name"] = "Tokyo"
            data["country"] = "Japan"
            val addedDocRef: Task<DocumentReference> = Firebase.firestore.collection("cities").add(data).addOnSuccessListener {
                val id = it.id
                Log.d("keyGen", "Id = $id")
            }

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