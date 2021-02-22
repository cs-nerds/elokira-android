package com.example.elokira.repositories

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BuilderClass () {
    companion object {
        const val BASE_URL = "https://api.elokiravote.ml"
//        http://192.168.100.14:8082

        private val gson: Gson = GsonBuilder()
            .create()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(CreateUser::class.java)


    }
}