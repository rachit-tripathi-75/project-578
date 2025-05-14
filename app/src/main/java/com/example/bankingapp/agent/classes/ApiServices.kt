package com.example.bankingapp.agent.classes

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

}