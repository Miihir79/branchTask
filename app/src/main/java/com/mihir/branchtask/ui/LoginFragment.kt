package com.mihir.branchtask.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mihir.branchtask.*
import com.mihir.branchtask.databinding.FragmentLoginBinding
import com.mihir.branchtask.network.AppObjectController
import com.mihir.branchtask.network.NetworkHelper
import com.mihir.branchtask.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val binding : FragmentLoginBinding by lazy { FragmentLoginBinding.inflate(layoutInflater) }

    private val viewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // directing user to chat list screen if AUTH key is generated
        lifecycleScope.launch {
            if (AppObjectController.read(requireContext(), AUTH).isNullOrEmpty().not()){
                findNavController().navigate(R.id.action_loginFragment_to_chatListFragment)
            }
        }

        setObservables()

        with(binding) {
            submitButton.setOnClickListener {
                if (NetworkHelper(requireContext()).isNetworkConnected()) {
                    if (editTextTextEmailAddress.text.isNullOrEmpty().not()) {
                        viewModel.login(editTextTextEmailAddress.text.toString(), editTextTextEmailAddress.text.reversed().toString())
                    } else {
                        showToast("Please enter the required fields")
                    }
                } else {
                    showToast("Please check your Internet Connection")
                }
            }

        }
    }

    private fun setObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.apiStatus.collectLatest { message->
                when(message){
                    API_SUCCESS -> {
                        findNavController().navigate(R.id.action_loginFragment_to_chatListFragment)
                    }
                    API_FAILED -> {
                        showToast("Oops something went wrong!")
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.authToken.collectLatest {
                lifecycleScope.launch {
                    if (it != null) {
                        AppObjectController.save(requireContext(), AUTH, it)
                    }
                }
            }
        }
    }

}