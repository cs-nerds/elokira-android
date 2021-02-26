package com.example.elokira.repositories

import AuthToken
import Authenticate
import LoginRequest
import User
import VerifiedUser
import com.example.elokira.data.Election
import com.example.elokira.data.Position
import com.example.elokira.data.UserDetails
import com.example.elokira.data.UserResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CreateUser {
//    /users/verify
    @POST("/users/verify")
   fun verifyUser(@Body voter: User): Call<UserResponse>

   @POST("/users")
   fun createUser(@Body verifiedUser: VerifiedUser): Call<Authenticate>

   @POST("/users/login/request")
   fun loginUser(@Body loginUserId: LoginRequest): Call<Authenticate>

   @POST("/users/login")
   fun authenticateUser(@Body loginUser: Authenticate): Call<AuthToken>

   @GET("/users/me")
   fun getUser(@Header("Authorization") bearer:String ):Call<UserDetails>

   @GET("/elections")
   fun getElections(@Header("Authorization") bearer:String ):Call<List<Election>>

   @POST("/elections/participate")
   fun postElections(@Body election: Election, @Header("Authorization") bearer: String): Call<ResponseBody>

   @GET
   fun getElectionPositions(@Url url: String, @Header("Authorization") bearer: String): Call<List<Position>>

}