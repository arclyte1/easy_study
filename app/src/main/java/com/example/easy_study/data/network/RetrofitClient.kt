package com.example.easy_study.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val baseUrl = "http://10.0.2.2:8000/api/v1/" // TODO change host

    private val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: UserService by lazy {
        retrofitClient
            .build()
            .create(UserService::class.java)
    }
}