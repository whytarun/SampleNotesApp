package com.example.samplenotesapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.samplenotesapp.api.UserApi
import com.example.samplenotesapp.models.UserRequest
import com.example.samplenotesapp.models.UserResponse
import com.example.samplenotesapp.utils.Constants.TAG
import com.example.samplenotesapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private var _userResponseLiveData =MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData :LiveData<NetworkResult<UserResponse>>
    get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val responseSignup =userApi.signup(userRequest)
        handleResponse(responseSignup)

    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun loginUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val responseLogin =userApi.signIn(userRequest)
        handleResponse(responseLogin)
    }
}