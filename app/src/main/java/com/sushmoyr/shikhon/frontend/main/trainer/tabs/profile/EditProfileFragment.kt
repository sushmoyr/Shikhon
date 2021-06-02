package com.sushmoyr.shikhon.frontend.main.trainer.tabs.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.databinding.FragmentEditProfileBinding
import com.sushmoyr.shikhon.frontend.main.trainer.bindingadapters.DataBindingAdapters.Companion.sourceUrl
import com.sushmoyr.shikhon.utils.Constants.UPLOAD_COVER_IMAGE_REQUEST_CODE
import com.sushmoyr.shikhon.utils.Constants.UPLOAD_PROFILE_IMAGE_REQUEST_CODE
import java.util.*


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditProfileFragmentArgs>()

    private val model: EditProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.user = args.user
        val user = args.user
        model.setProfileImageUri(user.profilePicUri)
        model.setCoverImageUri(user.coverPhotoUri)

        model.profileImageUri.observe(viewLifecycleOwner, { uri ->
            binding.profilePhoto.sourceUrl(uri)
        })

        model.coverImageUri.observe(viewLifecycleOwner, { uri ->
            binding.coverPhoto.sourceUrl(uri)
        })

        val gender = arrayOf("Male", "Female", "Not Specified")
        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            gender
        )
        binding.newGender.setAdapter(adapter)

        binding.birthDate.setOnClickListener {
            // Create the date picker builder and set the title
            val builder = MaterialDatePicker.Builder.datePicker()
                .also {
                    it.setTitleText("Pick Birth Date")
                }


            // create the date picker
            val datePicker = builder.build()

            // set listener when date is selected
            datePicker.addOnPositiveButtonClickListener {

                // Create calendar object and set the date to be that returned from selection
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = Date(it)
                binding.birthDate.text = Editable.Factory.getInstance().newEditable(
                    ("${calendar.get(Calendar.DAY_OF_MONTH)}- " +
                            "${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)}")
                )

            }

            datePicker.show(requireActivity().supportFragmentManager, "MyTAG")
        }

        binding.updateUserInfoButton.setOnClickListener {
            updateInfo()
            hideViews()
        }

        binding.updateCoverButton.setOnClickListener {
            uploadCoverPhoto()
        }

        binding.updateProfilePicBtn.setOnClickListener {
            uploadProfilePhoto()
        }

        return binding.root
    }

    private fun uploadProfilePhoto() {
        val intent = Intent()
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, UPLOAD_PROFILE_IMAGE_REQUEST_CODE)
    }

    private fun uploadCoverPhoto() {
        val intent = Intent()
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, UPLOAD_COVER_IMAGE_REQUEST_CODE)
    }

    private fun updateInfo() {
        val name = binding.newName.text.toString()
        val bio: String = binding.newBio.text.toString()
        val gender = binding.newGender.text.toString()
        val birthdate = binding.birthDate.text.toString()
        val contactNo = binding.newContactNo.text.toString()
        val coverPhotoUri = model.coverImageUri.value
        val profileImageUri = model.profileImageUri.value


        model.updateUser(
            name,
            bio,
            gender,
            birthdate,
            contactNo,
            coverPhotoUri,
            profileImageUri,
            args.user
        ).addOnSuccessListener {
            Toast.makeText(requireContext(), "Update Successful", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)

        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Update Failed. Reason: ${it.message}", Toast.LENGTH_SHORT).show()
            showView()
        }
    }

    private fun hideViews(){
        binding.progressBg.visibility = View.VISIBLE
        binding.updateProgress.visibility = View.VISIBLE
        binding.updateProfilePicBtn.visibility = View.INVISIBLE
        binding.updateCoverButton.visibility = View.INVISIBLE
    }

    private fun showView(){
        binding.progressBg.visibility = View.INVISIBLE
        binding.updateProgress.visibility = View.INVISIBLE
        binding.updateProfilePicBtn.visibility = View.VISIBLE
        binding.updateCoverButton.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                UPLOAD_COVER_IMAGE_REQUEST_CODE -> {
                    if (data?.data != null) {
                        val imageUri: Uri = data.data!!
                        model.setCoverImageUri(imageUri)
                    }
                }

                UPLOAD_PROFILE_IMAGE_REQUEST_CODE -> {
                    if (data?.data != null) {
                        val imageUri: Uri = data.data!!
                        model.setProfileImageUri(imageUri)
                    }
                }
            }
        }

    }

}