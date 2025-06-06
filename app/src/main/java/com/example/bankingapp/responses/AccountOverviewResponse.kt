package com.example.bankingapp.responses

import com.google.gson.annotations.SerializedName

data class AccountOverviewResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: AccountDetails
)


data class AccountDetails(
    @SerializedName("Cid")
    val cid: String,
    @SerializedName("AccNoInt")
    val accNoInt: String,
    @SerializedName("Typeofacc")
    val typeOfAcc: String,
    @SerializedName("MemId")
    val memId: String,
    @SerializedName("CIFNO")
    val cifNo: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Father")
    val father: String,
    @SerializedName("Mobile")
    val mobile: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("PresentAddress")
    val presentAddress: String,
    @SerializedName("PermanentAddress")
    val permanentAddress: String,
    @SerializedName("Period")
    val period: String,
    @SerializedName("emi")
    val emi: String,
    @SerializedName("Mvalue")
    val mValue: String,
    @SerializedName("Rate")
    val rate: String,
    @SerializedName("mbal")
    val mbal: String,
    @SerializedName("mbalcharge")
    val mbalCharge: String,
    @SerializedName("REMARKS")
    val remarks: String,
    @SerializedName("nominee")
    val nominee: String,
    @SerializedName("nrelation")
    val nRelation: String,
    @SerializedName("agent")
    val agent: String,
    @SerializedName("status")
    val accountStatus: String,
    @SerializedName("SANCTIONEDLIMIT")
    val sanctionedLimit: String,
    @SerializedName("SANCTIONEDperiod")
    val sanctionedPeriod: String,
    @SerializedName("Sdocument")
    val sDocument: String,
    @SerializedName("mdate")
    val mDate: String,
    @SerializedName("datenew")
    val dateNew: String,
    @SerializedName("otherinform")
    val otherInform: String?,
    @SerializedName("introby")
    val introBy: String,
    @SerializedName("operationmode")
    val operationMode: String,
    @SerializedName("IsSMS")
    val isSMS: String,
    @SerializedName("IsNB")
    val isNB: String,
    @SerializedName("VAccount")
    val vAccount: String,
    @SerializedName("cf_terminal_id")
    val cfTerminalId: String,
    @SerializedName("subenach")
    val subEnach: String?,
    @SerializedName("VAccountaub")
    val vAccountAub: String,
    @SerializedName("cf_subscription_id")
    val cfSubscriptionId: String
)