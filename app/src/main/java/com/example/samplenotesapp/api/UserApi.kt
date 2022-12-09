package com.example.samplenotesapp.api

import com.example.samplenotesapp.models.UserRequest
import com.example.samplenotesapp.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/users/signup")
    suspend fun signup(@Body userRequest: UserRequest) :Response<UserResponse>

    @POST("/users/signin")
    suspend fun signIn(userRequest: UserRequest) :Response<UserResponse>
}