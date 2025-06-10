package com.example.bankingapp.responses

import com.google.gson.annotations.SerializedName

data class AccountStatementDetailResponse(
    val status: String,
    @SerializedName("user_detail")
    val userDetail: UserDetail,
    @SerializedName("statement_data")
    val statementData: List<StatementData>
)

data class UserDetail(
    @SerializedName("Member-Id")
    val memberId: String,
    @SerializedName("Account_Type")
    val accountType: String,
    @SerializedName("Account_Number")
    val accountNumber: String,
    @SerializedName("Account_Opening_Date")
    val accountOpeningDate: String,
    @SerializedName("Applicant_Name")
    val applicantName: String,
    @SerializedName("Contact_No")
    val contactNo: String,
    @SerializedName("Virtual_Account")
    val virtualAccount: String,
    @SerializedName("Father_Name")
    val fatherName: String,
    @SerializedName("IFSC_Code")
    val ifscCode: String,
    @SerializedName("Address")
    val address: String,
    @SerializedName("Bank_Name")
    val bankName: String,
    @SerializedName("Print_Date")
    val printDate: String
)

data class StatementData(
    @SerializedName("s_no")
    val sno: Int,
    @SerializedName("Date")
    val date: String,
    val description: String,
    @SerializedName("instument_no")
    val instrumentNo: String,
    val debit: String,
    val credit: String,
    val balance: String,
    @SerializedName("dr_cr")
    val drCr: String
)