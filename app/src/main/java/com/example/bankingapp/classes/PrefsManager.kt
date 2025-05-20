package com.example.bankingapp.classes

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.bankingapp.responses.LoginResponse
import com.google.gson.Gson

class PrefsManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }


    companion object {
        const val PREF_NAME = "MyPrefs"
        const val PIN_SHOW_PREF_NAME = "pinBottomSheetDialogPrefs"

        fun setSession(context: Context, flag: Boolean) {
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit() {
                putBoolean("isLoggedIn", flag)
            }
        }

        fun getSession(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("isLoggedIn", false)
        }

        fun setUserInformation(context: Context, loginResponse: LoginResponse) {
            val gson = Gson()
            val userJsonInformationString = gson.toJson(loginResponse)
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit() {
                putString("userInformation", userJsonInformationString)
            }
        }

        fun getUserInformation(context: Context) : LoginResponse {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val jsonFromPrefs = sharedPreferences.getString("userInformation", null)
            val userInformationObject = gson.fromJson(jsonFromPrefs, LoginResponse::class.java)
            return userInformationObject
        }

        fun setUserType(context: Context, userType: Int) { // 0 --> for customer, 1 --> for agent
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit() {
                putInt("userType", userType)
            }
        }

        fun getUserType(context: Context): Int {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("userType", -1)
        }

        fun setCreatedmPinFlag(context: Context, flag: Boolean) { // true --> user has created mPIN
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit() {
                putBoolean("mPinFlag", flag)
            }
        }

        fun hasCreatedmPin(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("mPinFlag", false)
        }

        fun setShouldShowCreatePinBottomSheetDialog(context: Context, flag: Boolean) { // false --> user has created mPIN
            context.getSharedPreferences(PIN_SHOW_PREF_NAME, Context.MODE_PRIVATE).edit() {
                putBoolean("pinCreateBottomSheetDialog", flag)
            }
        }

        fun shouldShowCreatePinBottomSheetDialog(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(PIN_SHOW_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("pinCreateBottomSheetDialog", true)
        }



    }
}