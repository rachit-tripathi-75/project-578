package com.example.bankingapp.classes

import com.example.bankingapp.responses.LoginResponse
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

}