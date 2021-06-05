package com.sushmoyr.shikhon.frontend.main.messenger

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sushmoyr.shikhon.backend.data.Message
import com.sushmoyr.shikhon.backend.data.User
import com.sushmoyr.shikhon.backend.repository.FirebaseRepository
import com.sushmoyr.shikhon.databinding.FragmentChatBinding
import com.sushmoyr.shikhon.frontend.main.trainer.bindingadapters.DataBindingAdapters.Companion.sourceUrl
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import java.time.LocalDateTime


class ChatFragment : Fragment() {

    private val adapter = GroupAdapter<GroupieViewHolder>()

    private val model: MessengerViewModel by activityViewModels()

    private val args: ChatFragmentArgs by navArgs()

    private val currentUser = Firebase.auth.currentUser?.uid.toString()

    private lateinit var sentUser: User
    private lateinit var receivedUser: User

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.chats.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        binding.chats.layoutManager = layoutManager
        binding.chats.scrollToPosition(adapter.itemCount - 1)

        val room = args.room

        observeUser()

        observeMessages()

        binding.messageSendBtn.setOnClickListener {
            if(binding.chat.text.isNullOrEmpty()){

            }
            else{
                val msg = binding.chat.text.toString()
                binding.chat.text?.clear()
                val sendTime = getCurrentTime()
                val message = Message(currentUser, sendTime, msg)
                FirebaseRepository.addNewMessage(room.id, sendTime, message)
            }
        }



        return binding.root
    }

    private fun observeMessages() {
        val room = args.room
        if(room.id.isEmpty()){
            Log.d("messenger", "Empty value")
            return
        }

        val messages = model.getMessages(room.id)
        messages.observe(viewLifecycleOwner, {messages->
            val groups : MutableList<Group> = mutableListOf()
            messages.forEach {

                if (it.senderId == currentUser){
                    groups.add(SendMessageItem(it))
                }
                else{
                    groups.add(ReceiveMessageItem(it))
                }
                Log.d("messenger", "${it.message}, ${it.senderId}")
            }
            adapter.clear()
            adapter.addAll(groups)
        })

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }


        binding.chatReceiverName.setOnClickListener {
            if(receivedUser.uuid.isNotEmpty()){
                val direction = ChatFragmentDirections.actionChatFragmentToVisitedProfileFragment(receivedUser.uuid)
                findNavController().navigate(direction)
            }
        }



    }

    private fun observeUser() {

        val room = args.room

        model.userList.observe(viewLifecycleOwner, { users->
            users.forEach {
                if(room.chatOwners.contains(it.uuid)){
                    if(it.uuid == currentUser)
                        sentUser = it
                    else{
                        receivedUser = it
                        binding.chatReceiverPP.sourceUrl(it.profilePicUri)
                        binding.chatReceiverName.text = it.name
                    }
                }
            }
        })
    }


    private fun getCurrentTime(): String {
        return LocalDateTime.now().toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

