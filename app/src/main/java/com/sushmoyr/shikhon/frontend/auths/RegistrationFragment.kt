package com.sushmoyr.shikhon.frontend.auths

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.databinding.FragmentRegistrationBinding
import com.sushmoyr.shikhon.frontend.initials.AccountLoaderActivity
import com.sushmoyr.shikhon.frontend.main.trainee.TraineeActivity
import com.sushmoyr.shikhon.frontend.main.trainer.TrainerActivity
import com.sushmoyr.shikhon.utils.Constants
import com.sushmoyr.shikhon.utils.Verify

class RegistrationFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    private var isDataOk = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        listenToUiChanges()

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        return binding.root
    }

    private fun enableLoading(enable: Boolean){
        if(enable){
            binding.registrationInProgress.visibility = View.VISIBLE
            binding.registerBtn.isClickable = false
        }
        else{
            binding.registrationInProgress.visibility = View.GONE
            binding.registerBtn.isClickable = true
        }
    }

    override fun onStart() {
        super.onStart()

        binding.registerBtn.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                enableLoading(true)
                registerUser()
            }
        }
    }

    private fun listenToUiChanges(){
        Verify.checkCurrentPassword(binding.password)
        Verify.matchPassword(binding.confirmPassword, binding.password.text.toString())
    }

    private fun registerUser() {
        val name = binding.name.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()
        val acceptLicense = binding.termsCheckbox.isChecked

        if (verifyInfo(name, password, confirmPassword, acceptLicense)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Firebase", "createUserWithEmail:success")
                        val user = auth.currentUser
                        addUserToDatabase(user)
                        val profileUpdates = userProfileChangeRequest {
                            displayName = Constants.USER_TYPE_TRAINEE.toString()
                        }

                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("profileUpdate", "User profile updated.")
                                    completeProfile()
                                }
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("Firebase", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(), "Registration failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        enableLoading(false)
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Wrong info", Toast.LENGTH_SHORT).show()
        }

    }

    private fun completeProfile() {
        val intent = Intent(requireActivity(), AccountLoaderActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun addUserToDatabase(user: FirebaseUser?) {
        if (user != null) {
            val uid = user.uid
            val email = user.email.toString()
            val name = binding.name.text.toString()
            val accountType = Constants.USER_TYPE_TRAINEE
            val profilePicUri = Constants.DEFAULT_PROFILE_PIC_URI
            val newUser = User(uid, name, email, accountType, profilePicUri)
            authViewModel.registerUser(newUser)
        }
    }

    private fun verifyInfo(
        name: String,
        password: String,
        confirmPassword: String,
        license: Boolean
    ): Boolean {
        return password == confirmPassword && license && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(
            confirmPassword
        ) && !TextUtils.isEmpty(name)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}