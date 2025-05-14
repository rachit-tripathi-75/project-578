package com.example.bankingapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.MainActivity
import com.example.bankingapp.R
import com.example.bankingapp.agent.networks.NetworkChangeReceiver
import com.example.bankingapp.classes.ApiClient
import com.example.bankingapp.classes.PrefsManager
import com.example.bankingapp.databinding.ActivityAccountOverviewBinding
import com.example.bankingapp.models.AccountDetailModel
import com.example.bankingapp.responses.AccountsDetailResponse
import com.example.bankingapp.responses.LoginResponse
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountOverviewActivity : AppCompatActivity() {


    var networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
        override fun onNetworkConnected() {
            binding.llNoInternetFound.visibility = View.GONE
            binding.clAccountOverview.visibility = View.VISIBLE
            fetchAssociatedAccountNumbers()
            Log.d("networkInterceptorTAG", "inside onNetworkConnected()")

        }

        override fun onNetworkDisconnected() {
            binding.clAccountOverview.visibility = View.GONE
            binding.llNoInternetFound.visibility = View.VISIBLE
            Log.d("networkInterceptorTAG", "inside onNetworkDisconnected()")
            Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_LONG).show()
        }
    })



    private lateinit var binding: ActivityAccountOverviewBinding
    private var accounts: MutableList<AccountDetailModel> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()
        listeners()

    }

    private fun initialisers() {
        fetchAssociatedAccountNumbers()
    }

    private fun listeners() {

    }


    private fun fetchAssociatedAccountNumbers() {

        binding.cvAccountNumber.visibility = View.GONE
        binding.progressbarMain.visibility = View.VISIBLE

        ApiClient.getAccountDetailsInstance.getAccountDetails(
            "Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
            "ci_session=bfur80hklc6bks0jthl51vaojjq6dtfp",
            PrefsManager.getUserInformation(this@AccountOverviewActivity).data.memId).enqueue(object : Callback<AccountsDetailResponse> {


            override fun onResponse(call: Call<AccountsDetailResponse?>, response: Response<AccountsDetailResponse?>) {
                binding.cvAccountNumber.visibility = View.VISIBLE
                binding.progressbarMain.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d("fetchAssociatedAccountNumbersTAG", response.body()?.message.toString())
                    val s = response.body()
                    if (s?.status == 1) {
                        accounts.clear()
                        Log.d("fetchAssociatedAccountNumbersTAG", "data size: " + response.body()?.data!!.size)
                        for (i in 0 until s.data.size) {
                            accounts.add(AccountDetailModel(s.data[i].idAuto, s.data[i].cid, s.data[i].accountNumberInternal, s.data[i].accountType, s.data[i].memberId, s.data[i].accountType, s.data[i].terminalId, s.data[i].subscriptionId, s.data[i].name, s.data[i].mobile, s.data[i].virtualAccount))
                        }
                        makeAccountNumberSpinner()
                    }

                } else {
                    Log.d("fetchAssociatedAccountNumbersTAG", response.body()?.message.toString())
                    Toast.makeText(this@AccountOverviewActivity, "Incorrect Credentials", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AccountsDetailResponse?>, t: Throwable) {
                binding.cvAccountNumber.visibility = View.VISIBLE
                binding.progressbarMain.visibility = View.GONE
                Log.d("fetchAssociatedAccountNumbersTAG", "Error: ${t.message}")
                Toast.makeText(this@AccountOverviewActivity, "An error has occurred. Please try again later", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun makeAccountNumberSpinner() {


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,accounts.map { "${it.accType}${it.accToInt}" } // Format display text
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerAccountNumber.adapter = adapter

        // Handle item selection
//        binding.spinnerAccountNumber.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                val selectedAccount = accounts[position]
//                // Handle selected account
//                Toast.makeText(
//                    this@AccountOverviewActivity,
//                    "Selected: ${selectedAccount.accType} (${selectedAccount.accToInt})",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Handle no selection
//            }
//        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onResume() {
        super.onResume()
        NetworkChangeReceiver.registerReceiver(this, networkChangeReceiver)
    }

    override fun onPause() {
        super.onPause()
        NetworkChangeReceiver.unregisterReceiver(this, networkChangeReceiver)
    }


}