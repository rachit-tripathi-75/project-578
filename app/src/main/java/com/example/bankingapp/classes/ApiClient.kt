package com.example.bankingapp.classes

import okhttp3.internal.notify
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiClient {
    const val BASE_URL = "https://webapp.gccsl.in/"

    val loginInstance: ApiServices.LoginApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.LoginApiService::class.java)
    }

    val loginWithMPinInstance: ApiServices.LoginWithMPinApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.LoginWithMPinApiService::class.java)
    }

    val getAccountDetailsInstance: ApiServices.AccountsDetailApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.AccountsDetailApiService::class.java)
    }


}