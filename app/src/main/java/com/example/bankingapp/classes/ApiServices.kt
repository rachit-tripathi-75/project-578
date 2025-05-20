package com.example.bankingapp.classes

import com.example.bankingapp.responses.AccountsDetailResponse
import com.example.bankingapp.responses.LoginResponse
import okhttp3.Cookie
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming

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