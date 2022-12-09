package com.example.samplenotesapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.samplenotesapp.R
import com.example.samplenotesapp.databinding.FragmentLoginBinding
import com.example.samplenotesapp.models.UserRequest
import com.example.samplenotesapp.utils.NetworkResult
import com.example.samplenotesapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel> ()
    @Inject
    private lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentLoginBinding.inflate(inflater,container,false)
         if(tokenManager.getToken() != null){
             findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
         }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            val validateResult =validateInputs()
            if(!validateResult.first){
                var userRequest =getUserRequest()
                authViewModel.registerUser(userRequest)
            }else {
                binding.txtError.text =validateResult.second
            }
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
        binding.btnSignUp.setOnClickListener {
           findNavController().popBackStack()
        }

        bindingObserver()
    }

    private fun bindingObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun validateInputs() :Pair<Boolean,String>{
        val userRequest = getUserRequest()
        return authViewModel.validateCredential(userRequest.username,userRequest.email,userRequest.password,true)
    }

    private fun getUserRequest(): UserRequest {
        var emailAddress = binding.txtEmail.text.toString()
        var password = binding.txtPassword.text.toString()
        return UserRequest( emailAddress, password,"")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }

   }