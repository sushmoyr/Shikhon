package com.sushmoyr.shikhon.frontend.main.messenger

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.backend.data.ChatInstance
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.databinding.FragmentMessengerBinding
import java.time.LocalDateTime

class MessengerFragment : Fragment() {

    private var _binding: FragmentMessengerBinding? = null
    private val binding get() = _binding!!

    private val auth = Firebase.auth

    private val model: MessengerViewModel by activityViewModels()

    private val adapter: MessengerAdapter by lazy {
        MessengerAdapter(auth.currentUser?.uid.toString()){
            Log.d("messenger", "OnClicked: $it")
            val directions = MessengerFragmentDirections.actionMessengerFragmentToChatFragment(it)
            findNavController().navigate(directions)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMessengerBinding.inflate(inflater, container, false)

        setUpRecyclerView()

        Log.d("messenger", auth.currentUser?.uid.toString())
        model.userRooms.observe(viewLifecycleOwner, { roomData ->
            roomData.forEach {
                Log.d("messenger", it.toString())
            }
            adapter.setRoomData(roomData)
        })

        model.userList.observe(viewLifecycleOwner, {users ->
            adapter.setUserData(users)
        })


        return binding.root
    }

    private fun setUpRecyclerView() {
        val rv = binding.messageListRv
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up action bar
        val toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Chats"
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

