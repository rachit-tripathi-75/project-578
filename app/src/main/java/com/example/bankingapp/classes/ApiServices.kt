package com.example.bankingapp.classes

import com.example.bankingapp.responses.AccountsDetailResponse
import com.example.bankingapp.responses.CreateMPinResponse
import com.example.bankingapp.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

class ApiServices {
    interface LoginApiService {
        @FormUrlEncoded
        @POST("API/login")
        fun login(
            @Header("Cookie") cookie: String,
            @Field("mid") mid: String,
            @Field("password") password: String
        ): Call<LoginResponse>
    }

    interface LoginWithMPinApiService {
        @FormUrlEncoded
        @POST("API/loginmpin")
        fun loginWithMPin(
            @Header("Authorization") authorization: String,
            @Header("Cookie") cookie: String,
            @Field("mpin") mPin: String,
            @Field("mobileid") mobileId: String
        ): Call<LoginResponse>
    }

    interface CreateMPinApiService {
        @FormUrlEncoded
        @POST("API/insertmpin")
        fun createMPin(
            @Header("Authorization") authorization: String,
            @Header("Cookie") cookie: String,
            @Field("mpin") mPin: String,
            @Field("mobileid") androidId: String,
            @Field("memid") memId: String
        ): Call<CreateMPinResponse>
    }

    interface AccountsDetailApiService {
        @FormUrlEncoded
        @POST("API/getAccountsdetail")
        fun getAccountDetails(
            @Header("Authorization") authorization: String,
            @Header("Cookie") cookie: String,
            @Field("memid") memId: String
        ): Call<AccountsDetailResponse>
    }

}