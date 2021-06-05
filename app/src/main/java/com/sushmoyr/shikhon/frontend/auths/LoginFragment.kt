package com.sushmoyr.shikhon.frontend.auths

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.databinding.FragmentLoginBinding
import com.sushmoyr.shikhon.frontend.initials.PreTaskActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val toastTag = "Login Fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.createNewAccountBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        binding.forgetPassBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
        binding.loginBtn.setOnClickListener {
            loginUser()
        }


        return binding.root
    }

    private fun enableLoading(enable: Boolean){
        if(enable){
            binding.loginInProgress.visibility = View.VISIBLE
            binding.loginProgressBg.visibility = View.VISIBLE
            binding.loginBtn.isClickable = false
        }
        else{
            binding.loginInProgress.visibility = View.GONE
            binding.loginProgressBg.visibility = View.GONE
            binding.loginBtn.isClickable = true
        }
    }

    private fun loginUser() {
        val email = binding.email.text.toString().trimEnd()
        val password = binding.password.text.toString()

        if(disallowed(email, password)){
            Toast.makeText(requireContext(), "Fields can't be empty", Toast.LENGTH_SHORT).show()
            return
        }
        enableLoading(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(toastTag, "Login success")
                    startActivity(Intent(activity, PreTaskActivity::class.java))
                } else {
                    Log.d(toastTag, "Login failed")
                    Toast.makeText(requireContext(), "Login Failed. Try again later!!!", Toast.LENGTH_SHORT).show()
                    enableLoading(false)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}