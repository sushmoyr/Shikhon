package com.sushmoyr.shikhon.frontend.main.trainer.tabs.search.viewpagertabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.databinding.FragmentUserSearchResultBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.search.SearchFragmentDirections
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.search.SearchViewModel
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.search.UserListAdapter


class UserSearchResultFragment : Fragment() {

    private var _binding: FragmentUserSearchResultBinding? = null
    private val binding get() = _binding!!

    private val model: SearchViewModel by activityViewModels()

    private val userListAdapter : UserListAdapter by lazy {
        UserListAdapter{
            val auth = Firebase.auth
            if(auth.currentUser?.uid != it){
                val action = SearchFragmentDirections.actionSearchFragmentToVisitedProfileFragment(it)
                findNavController().navigate(action)
            }
            else
                findNavController().navigate(R.id.action_global_profileFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserSearchResultBinding.inflate(inflater, container, false)

        setUpRecyclerView()

        model.filteredUser.observe(viewLifecycleOwner, { user->
            userListAdapter.setData(user)
        })

        // Add document data with auto-generated id.
        // Add document data with auto-generated id.



        return binding.root
    }

    private fun setUpRecyclerView() {
        val rv = binding.userSearchResultRv
        rv.adapter = userListAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}