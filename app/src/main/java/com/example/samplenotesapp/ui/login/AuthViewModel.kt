package com.example.samplenotesapp.ui.login

import android.text.TextUtils
import android.util.Patterns
import android.util.Patterns.EMAIL_ADDRESS
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplenotesapp.models.UserRequest
import com.example.samplenotesapp.models.UserResponse
import com.example.samplenotesapp.repository.UserRepository
import com.example.samplenotesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) :ViewModel() {

    val userResponseLiveData :LiveData<NetworkResult<UserResponse>>
    get() = userRepository.userResponseLiveData
    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateCredential(userName :String, emailAddress:String, password :String, isLogin :Boolean) :Pair<Boolean,String>{
        var result =Pair(true, "")
        if(( !isLogin &&TextUtils.isEmpty(userName)) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password))
        {
            result = Pair(false, "please provide credentials" )
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
                result =Pair(false, "Please provide valid email address")
        } else if (password.length <6){
            result =Pair(false, " password should not be less than 6 ")
        }
        return result
    }
}