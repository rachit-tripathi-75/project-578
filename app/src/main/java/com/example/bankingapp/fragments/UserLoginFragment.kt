package com.example.bankingapp.fragments

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bankingapp.MainActivity
import com.example.bankingapp.R
import com.example.bankingapp.activities.SplashScreenActivity
import com.example.bankingapp.classes.ApiClient
import com.example.bankingapp.classes.ApiServices
import com.example.bankingapp.classes.PrefsManager
import com.example.bankingapp.databinding.FragmentUserLoginBinding
import com.example.bankingapp.responses.LoginResponse
import retrofit2.Call
import kotlin.toString


class UserLoginFragment : Fragment() {

    private lateinit var binding: FragmentUserLoginBinding
    private var isPasswordVisible: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        listeners()
        return binding.root
    }

    private fun listeners() {
        binding.btnSignIn.setOnClickListener {
            if (isValidSignIn()) {
                signIn()
            }
        }

        binding.togglePasswordButton.setOnClickListener { view ->
            if (isPasswordVisible) {
                // Hide password......
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.togglePasswordButton.setImageResource(R.drawable.ic_visibility_off)
            } else {
                // Show password.........
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.togglePasswordButton.setImageResource(R.drawable.ic_visibility)
            }
            isPasswordVisible = !isPasswordVisible
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }

    }

    private fun isValidSignIn(): Boolean {
        if (binding.etId.text.toString().isEmpty()) {
            binding.etId.setError("Enter your user ID")
            return false
        } else if (binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.setError("Enter your password")
            return false
        }
        return true
    }

    private fun signIn() {
        binding.btnSignIn.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        ApiClient.loginInstance.login(
            "ci_session=4ko0di4js59huoo8vlkmqst62813i2jv",
            binding.etId.text.toString(),
            binding.etPassword.text.toString()).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                binding.btnSignIn.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d("loginResponseTAG", response.body()?.msg  .toString())
                    val loginData = response.body()
                    if (loginData?.status == 1) {
                        PrefsManager.setUserInformation(requireContext(), loginData)
                        PrefsManager.setSession(requireContext(), true)
                        PrefsManager.setUserType(requireContext(), 0) //  0 --> for user (customer)........
                        startActivity(Intent(requireContext(), SplashScreenActivity::class.java))
                        requireActivity().finish()
                    }

                } else {
                    Log.d("loginResponseTAG", response.body()?.msg.toString())
                    Toast.makeText(requireContext(), "Incorrect Credentials", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.btnSignIn.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                Log.d("loginResponseTAG", "Error: ${t.message}")
                Toast.makeText(requireContext(), "An error has occurred. Please try again later", Toast.LENGTH_SHORT).show()
            }
        })
    }



}