package com.mihir.branchtask.ui

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mihir.branchtask.*
import com.mihir.branchtask.databinding.FragmentChatListBinding
import com.mihir.branchtask.model.MessageItem
import com.mihir.branchtask.ui.adapter.MessageListAdapter
import com.mihir.branchtask.viewmodel.ChatViewModel
import kotlinx.coroutines.flow.collectLatest

class ChatListFragment : Fragment() {

    private val binding: FragmentChatListBinding by lazy { FragmentChatListBinding.inflate(layoutInflater) }

    private val viewModel: ChatViewModel by lazy { ViewModelProvider(this)[ChatViewModel::class.java] }

    private lateinit var adapter : MessageListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservables()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        binding.imageButtonExitApp.setOnClickListener {
            AlertDialog.Builder(requireContext()).setTitle("Are you sure you want to exit the app?")
                .setMessage("Continuing will result in logging out of the app, are you sure you want to proceed?")
                .setPositiveButton("Yes"){dialog,_ ->
                    viewModel.reset()
                    dialog.dismiss()
                    requireActivity().finish()
                }
                .setNegativeButton("No"){dialog,_ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }

    private fun addObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.apiStatus.collectLatest { message->
                when (message){
                    API_FAILED -> {
                        showToast("Oops something went wrong!")
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.messages.collectLatest { messageFromApi ->
                messageFromApi?.let { message ->
                    val grouped = message.groupBy { it.thread_id }
                    val firstMessageList = ArrayList<MessageItem>()
                    for(i in grouped.keys){
                        grouped[i]?.get(0)?.let { it1 -> firstMessageList.add(it1) }
                    }
                    adapter = MessageListAdapter { thread_id ->
                        val list = grouped[thread_id]
                        val bundle = bundleOf(LIST_ITEMS to (list as ArrayList<out Parcelable>))
                        findNavController().navigate(R.id.action_chatListFragment_to_chatFragment, bundle)
                    }
                    binding.rvChatList.adapter = adapter
                    adapter.setChatItems(firstMessageList)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getMessages()
    }

}