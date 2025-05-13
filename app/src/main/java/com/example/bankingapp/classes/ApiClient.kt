package com.example.bankingapp.classes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    const val BASE_URL = "https://webapp.gccsl.in/"

    val loginInstance: ApiServices.LoginApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.LoginApiService::class.java)
    }


}