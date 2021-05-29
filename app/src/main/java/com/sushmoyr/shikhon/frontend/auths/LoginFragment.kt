package com.sushmoyr.shikhon.frontend.auths

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.databinding.FragmentLoginBinding
import com.sushmoyr.shikhon.frontend.initials.PreTaskActivity
import com.sushmoyr.shikhon.utils.Constants
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val toastTag = "Login Fragment"

    private val authViewModel: AuthViewModel by activityViewModels()

    private val firebaseRepository = FirebaseRepository()

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

        /*authViewModel.userAccountType.observe(viewLifecycleOwner, {
            Log.d("update", "User Type Set = $it")
            val sharedPref =
                requireActivity().getSharedPreferences(Constants.ACCOUNT_TYPE_EDITOR_KEY, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt(Constants.ACCOUNT_TYPE_KEY, it)
            editor.apply()
        })*/

        saveUserType()


        return binding.root
    }

    private fun saveUserType() {

        val cu = auth.currentUser
        if(cu != null){
            lifecycleScope.launch {
                val user = firebaseRepository.getCurrentUserData(cu.uid)

                val sharedPref =
                    requireActivity().getSharedPreferences(Constants.ACCOUNT_TYPE_EDITOR_KEY, Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                if (user != null) {
                    editor.putInt(Constants.ACCOUNT_TYPE_KEY, user.accountType)
                }
                editor.apply()
            }
        }

    }

    private fun loginUser() {
        val email = binding.email.text.toString().trimEnd()
        val password = binding.password.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("update", "Login success")
                    startActivity(Intent(activity, PreTaskActivity::class.java))
                    authViewModel.setUserAccountType(auth.currentUser!!.uid)
                } else {
                    Log.d(toastTag, "Login failed")
                    Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}