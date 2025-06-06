package com.example.bankingapp.classes

import com.example.bankingapp.responses.AccountOverviewResponse
import com.example.bankingapp.responses.AccountsDetailResponse
import com.example.bankingapp.responses.CreateMPinResponse
import com.example.bankingapp.responses.LoginResponse
import com.example.bankingapp.responses.ShowAccountNumberResponse
import com.example.bankingapp.responses.TypesOfAccountResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

    interface AccountOverviewInformation {
        @FormUrlEncoded
        @POST("Api/search_by_acc_no")
        fun getAccountInformation(
            @Field("acc_no") accountNumber: String,
            @Field("acc_type") accountType: String
        ): Call<AccountOverviewResponse>
    }

    interface TypesOfAccount {
        @GET("Api/type_of_account")
        fun getTypesOfAccount() : Call<TypesOfAccountResponse>
    }

    interface ShowAccountNumber {
        @FormUrlEncoded
        @POST("Api/ShowAccountNo")
        fun getAccountNumber(
            @Field("Acctype") accountNumber: String,
        ): Call<ShowAccountNumberResponse>
    }

}