package com.sushmoyr.shikhon.frontend.main.trainer.tabs.extras

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.databinding.FragmentExtrasBinding
import com.sushmoyr.shikhon.frontend.initials.PreTaskActivity

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
            if (auth.currentUser != null) {
                auth.signOut()
                val intent = Intent(activity, PreTaskActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }

        }

        return binding.root
    }
}