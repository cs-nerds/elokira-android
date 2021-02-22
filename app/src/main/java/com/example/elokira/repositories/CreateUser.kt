package com.example.elokira.repositories

import Login
import User
import VerifiedUser
import com.example.elokira.data.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateUser {
//    /users/verify
    @POST("/users/verify")
   fun verifyUser(@Body voter: User): Call<UserResponse>

   @POST("/users")
   fun createUser(@Body verifiedUser: VerifiedUser): Call<Login>
}