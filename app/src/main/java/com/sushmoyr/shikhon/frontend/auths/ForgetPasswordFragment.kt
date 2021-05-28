package com.sushmoyr.shikhon.frontend.auths

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        binding.resetEmailButton.setOnClickListener {
            resetPassword()
        }

        return binding.root
    }

    private fun resetPassword() {
        var email = binding.resetEmail.text.toString()
        if (!TextUtils.isEmpty(email)) {
            email = reformatEmail(email)
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(requireContext(), "Email sent", Toast.LENGTH_SHORT).show()
                        Handler().postDelayed({
                            findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
                        }, 300)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Unknown Error Occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else
            Toast.makeText(requireContext(), "Email can't be empty", Toast.LENGTH_SHORT).show()
    }

    private fun reformatEmail(email: String):String
    {
        var newEmail = email.trimEnd()
        Log.d("Debug", newEmail)
        return newEmail
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}