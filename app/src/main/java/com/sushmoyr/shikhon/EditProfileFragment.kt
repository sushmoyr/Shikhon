package com.sushmoyr.shikhon

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.sushmoyr.shikhon.databinding.FragmentEditProfileBinding
import java.time.LocalDate
import java.util.*


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EditProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.user = args.user

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
                binding.birthDate.text = Editable.Factory.getInstance().newEditable(("${calendar.get(Calendar.DAY_OF_MONTH)}- " +
                        "${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)}"))

            }

            datePicker.show(requireActivity().supportFragmentManager, "MyTAG")
        }

        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.setOnNavigationItemReselectedListener {

        }

        return binding.root
    }

}