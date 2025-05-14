package com.example.bankingapp.responses

import com.google.gson.annotations.SerializedName

data class AccountsDetailResponse(
    @SerializedName("Msg") val message: String,
    @SerializedName("type") val type: String,
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: List<Account>
)

data class Account(
    @SerializedName("IdAuto") val idAuto: String,
    @SerializedName("Cid") val cid: String,
    @SerializedName("AccNoInt") val accountNumberInternal: String,
    @SerializedName("Typeofacc") val accountTypeCode: String,
    @SerializedName("MemberId") val memberId: String,
    @SerializedName("AccType") val accountType: String,
    @SerializedName("cf_terminal_id") val terminalId: String?,
    @SerializedName("cf_subscription_id") val subscriptionId: String,
    @SerializedName("Name") val name: String,
    @SerializedName("Mobile") val mobile: String,
    @SerializedName("VAccountaub") val virtualAccount: String
)