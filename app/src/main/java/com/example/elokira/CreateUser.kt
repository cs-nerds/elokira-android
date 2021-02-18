package com.example.elokira

import User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateUser {

    @POST("")
    fun createVoter(@Body voter: User): Call<User>
}