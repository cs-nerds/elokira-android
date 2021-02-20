package com.example.elokira.repositories

import User
import com.example.elokira.data.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateUser {
//    /users/verify
    @POST("/users/verify")
   fun createVoter(@Body voter: User): Call<UserResponse>
}