package com.mihir.branchtask.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mihir.branchtask.*
import com.mihir.branchtask.databinding.FragmentChatBinding
import com.mihir.branchtask.model.MessageItem
import com.mihir.branchtask.network.NetworkHelper
import com.mihir.branchtask.ui.adapter.ChatAdapter
import com.mihir.branchtask.viewmodel.ChatViewModel
import kotlinx.coroutines.flow.collectLatest

class ChatFragment : Fragment() {

    private val binding : FragmentChatBinding by lazy { FragmentChatBinding.inflate(layoutInflater) }

    private val viewModel: ChatViewModel by lazy { ViewModelProvider(this)[ChatViewModel::class.java] }

    private lateinit var adapter : ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getParcelableArrayList<MessageItem?>(LIST_ITEMS)
        addObservables()

        with(binding){
            imageButtonSend.setOnClickListener {
                if (NetworkHelper(requireContext()).isNetworkConnected()){
                    if (editTextTextSend.text.isNullOrEmpty().not()){
                        data?.get(0)?.let { it1 -> viewModel.sendMessages(it1.thread_id,editTextTextSend.text.toString()) }
                    }
                }else{
                    showToast("Please check your Internet Connection")
                }
            }
            imageButtonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            adapter = ChatAdapter()
            binding.rvChat.adapter = adapter
            if (data?.size!! <=1){
                adapter.setSingleData(data[0])
            }else{
                adapter.setData(data as ArrayList<MessageItem>)
            }
            binding.rvChat.smoothScrollToPosition(adapter.itemCount - 1)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

    }

    private fun addObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.apiStatus.collectLatest { message ->
                when (message){
                    API_FAILED -> {
                        showToast("Oops something went wrong!")
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.message.collectLatest { sentMessage ->
                if (sentMessage != null) {
                    adapter.addOurMessage(sentMessage)
                    binding.editTextTextSend.text.clear()
                    binding.rvChat.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }
        }

    }

}