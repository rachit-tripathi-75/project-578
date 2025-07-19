package com.example.bankingapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bankingapp.R
import com.example.bankingapp.adapters.ShowAccountNumberAdapter
import com.example.bankingapp.agent.networks.NetworkChangeReceiver
import com.example.bankingapp.classes.ApiClient
import com.example.bankingapp.classes.PrefsManager
import com.example.bankingapp.databinding.ActivityAccountOverviewBinding
import com.example.bankingapp.fragments.AccountOverviewFragment
import com.example.bankingapp.models.AccountDetailModel
import com.example.bankingapp.models.AccountNumberModel
import com.example.bankingapp.models.TypesOfAccountsModel
import com.example.bankingapp.responses.ShowAccountNumberResponse
import com.example.bankingapp.responses.TypesOfAccountResponse
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountOverviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountOverviewBinding
    private var isUserSelecting = false
    private var accountTypes: MutableList<TypesOfAccountsModel> = mutableListOf()
    private var accountNumbersList: MutableList<AccountNumberModel> = mutableListOf()
    private var selectedAccountTypeValue = ""
    private var selectedAccountNumberValue = ""

    var networkChangeReceiver: NetworkChangeReceiver =
        NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
            override fun onNetworkConnected() {
                binding.llNoInternetFound.visibility = View.GONE
                binding.clAccountOverview.visibility = View.VISIBLE
                fetchAssociatedAccountTypes()
            }

            override fun onNetworkDisconnected() {
                binding.clAccountOverview.visibility = View.GONE
                binding.llNoInternetFound.visibility = View.VISIBLE
                Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_LONG).show()
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialisers()
        listeners()
    }

    private fun initialisers() {
        fetchAssociatedAccountTypes()

        binding.actvShowAccountNumber.setOnItemClickListener { parent, view, position, id ->
            selectedAccountNumberValue = (parent.adapter as ShowAccountNumberAdapter).getItem(position).toString()
            Log.d("selectedAccountNumberTAG", "Selected account number: $selectedAccountNumberValue")
        }
    }

    private fun listeners() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // Add Go button click listener
        binding.btnGo.setOnClickListener {
            if (selectedAccountTypeValue.isNotEmpty() && selectedAccountNumberValue.isNotEmpty()) {
                showAccountDetailsFragment()
            } else {
                Toast.makeText(this, "Please select account type and number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAccountDetailsFragment() {
        val fragment = AccountOverviewFragment.newInstance(selectedAccountTypeValue, selectedAccountNumberValue)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

        // Hide the input section and show fragment container
        binding.llInputSection.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE
    }

    private fun fetchAssociatedAccountTypes() {
        binding.progressbarMain.visibility = View.VISIBLE

        ApiClient.getTypesOfAccount.getTypesOfAccount()
            .enqueue(object : Callback<TypesOfAccountResponse> {
                override fun onResponse(
                    call: Call<TypesOfAccountResponse?>,
                    response: Response<TypesOfAccountResponse?>
                ) {
                    binding.progressbarMain.visibility = View.GONE
                    if (response.isSuccessful) {
                        val s = response.body()
                        if (s?.status == "200") {
                            accountTypes.clear()
                            for (i in 0 until s.data.size) {
                                accountTypes.add(
                                    TypesOfAccountsModel(
                                        s.data[i].Id,
                                        s.data[i].Name,
                                        s.data[i].Abr
                                    )
                                )
                            }
                            makeAccountTypeSpinner()
                        }
                    }
                }

                override fun onFailure(call: Call<TypesOfAccountResponse?>, t: Throwable) {
                    binding.progressbarMain.visibility = View.GONE
                    Toast.makeText(this@AccountOverviewActivity, "Error loading account types", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun fetchShowAccountNumbers(accType: String) {
        binding.progressbarMain.visibility = View.VISIBLE

        ApiClient.getAccountNumber.getAccountNumber(accType).enqueue(object : Callback<ShowAccountNumberResponse> {
            override fun onResponse(call: Call<ShowAccountNumberResponse?>, response: Response<ShowAccountNumberResponse?>) {
                binding.progressbarMain.visibility = View.GONE
                if (response.isSuccessful) {
                    val s = response.body()
                    if (s?.status == "200" && s.data.isNotEmpty()) {
                        accountNumbersList.clear()
                        for (i in 0 until s.data.size) {
                            accountNumbersList.add(
                                AccountNumberModel(
                                    s.data[i].AccNoInt,
                                    s.data[i].Typeofacc,
                                    s.data[i].nominee,
                                    s.data[i].MemId,
                                    s.data[i].CIFNO,
                                    s.data[i].name,
                                    s.data[i].Father
                                )
                            )
                        }
                        makeAccountNumberSpinner()
                    }
                }
            }

            override fun onFailure(call: Call<ShowAccountNumberResponse?>, t: Throwable) {
                binding.progressbarMain.visibility = View.GONE
                Toast.makeText(this@AccountOverviewActivity, "Error loading account numbers", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun makeAccountTypeSpinner() {
        val adapter = ArrayAdapter(
            this,
            R.layout.custom_spinner_item,
            accountTypes.map { it.name }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerAccountType.adapter = adapter
        binding.spinnerAccountType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isUserSelecting) {
                    isUserSelecting = true
                    return
                }

                if (position > 0) {
                    val selectedAccountType = accountTypes[position]
                    selectedAccountTypeValue = selectedAccountType.id.toString()
                    fetchShowAccountNumbers(selectedAccountType.id)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun makeAccountNumberSpinner() {
        val adapter = ShowAccountNumberAdapter(this, accountNumbersList)
        binding.actvShowAccountNumber.setAdapter(adapter)
    }

    override fun onBackPressed() {
        if (binding.fragmentContainer.visibility == View.VISIBLE) {
            // If fragment is visible, hide it and show input section
            binding.fragmentContainer.visibility = View.GONE
            binding.llInputSection.visibility = View.VISIBLE
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
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