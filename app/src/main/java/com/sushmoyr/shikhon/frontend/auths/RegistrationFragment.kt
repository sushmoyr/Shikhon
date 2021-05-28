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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.databinding.FragmentRegistrationBinding
import com.sushmoyr.shikhon.frontend.main.trainer.TrainerActivity
import com.sushmoyr.shikhon.utils.Constants

class RegistrationFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.registerBtn.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                registerUser()
            }
        }
    }

    private fun registerUser() {
        val name = binding.name.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()
        val acceptLicense = binding.termsCheckbox.isChecked
        val accountType = getAccountType()

        if (verifyInfo(name, password, confirmPassword, acceptLicense, accountType)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Firebase", "createUserWithEmail:success")
                        val user = auth.currentUser
                        addUserToDatabase(user)
                        completeProfile()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Firebase", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Wrong info", Toast.LENGTH_SHORT).show()
        }

    }

    private fun completeProfile() {
        val intent = Intent(requireActivity(), TrainerActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun addUserToDatabase(user: FirebaseUser?) {
        if (user != null) {
            val uid = user.uid
            val email = user.email.toString()
            val name = binding.name.text.toString()
            val accountType = getAccountType()
            val profilePicUri = Constants.DEFAULT_PROFILE_PIC_URI
            val newUser = User(uid, name, email, accountType, profilePicUri)
            authViewModel.registerUser(newUser)
        }
    }

    private fun getAccountType(): Int {
        val chipId = binding.radioGroup.checkedChipId.toString()
        val trainee = R.id.chipTrainee.toString()
        val trainer = R.id.chipTrainer.toString()
        if (chipId == trainee)
            return Constants.USER_TYPE_TRAINEE
        else if (chipId == trainer)
            return Constants.USER_TYPE_TRAINER
        return Constants.USER_TYPE_NONE
    }

    private fun verifyInfo(
        name: String,
        password: String,
        confirmPassword: String,
        license: Boolean,
        accountType: Int
    ): Boolean {
        return password == confirmPassword && license && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(
            confirmPassword
        ) && !TextUtils.isEmpty(name) && accountType != Constants.USER_TYPE_NONE
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}