package com.example.bankingapp.classes

import okhttp3.internal.notify
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiClient {
    const val BASE_URL = "https://webapp.gccsl.in/"
    const val BASE_URL2 = "https://webapp2.gccsl.in/";

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

    val createMPinInstance: ApiServices.CreateMPinApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.CreateMPinApiService::class.java)
    }

//    val getAccountOverviewInformation: ApiServices.AccountOverviewInformation by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL2)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiServices.AccountOverviewInformation::class.java)
//    }

    val getAccountStatementDetail: ApiServices.AccountStatementDetail by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.AccountStatementDetail::class.java)
    }

    val getTypesOfAccount : ApiServices.TypesOfAccount by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.TypesOfAccount::class.java);
    }

    val getAccountNumber : ApiServices.ShowAccountNumber by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.ShowAccountNumber::class.java)
    }


}