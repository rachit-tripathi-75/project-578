package com.example.bankingapp.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.adapters.ShowAccountNumberAdapter
import com.example.bankingapp.adapters.StatementAdapter
import com.example.bankingapp.agent.networks.NetworkChangeReceiver
import com.example.bankingapp.classes.ApiClient
import com.example.bankingapp.classes.PrefsManager
import com.example.bankingapp.databinding.ActivityAccountOverviewBinding
import com.example.bankingapp.models.AccountDetailModel
import com.example.bankingapp.models.AccountNumberModel
import com.example.bankingapp.models.TypesOfAccountsModel
import com.example.bankingapp.responses.AccountOverviewResponse
import com.example.bankingapp.responses.AccountStatementDetailResponse
import com.example.bankingapp.responses.AccountsDetailResponse
import com.example.bankingapp.responses.ShowAccountNumberResponse
import com.example.bankingapp.responses.TypesOfAccountResponse
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountOverviewActivity : AppCompatActivity() {


    var networkChangeReceiver: NetworkChangeReceiver =
        NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
            override fun onNetworkConnected() {
                binding.llNoInternetFound.visibility = View.GONE
                binding.clAccountOverview.visibility = View.VISIBLE
//                fetchAssociatedAccountNumbers()
                fetchAssociatedAccountTypes()
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
    private var isUserSelecting = false
    private var accounts: MutableList<AccountDetailModel> = mutableListOf()
    private var accountTypes: MutableList<TypesOfAccountsModel> = mutableListOf();
    private var accountNumbersList: MutableList<AccountNumberModel> = mutableListOf();
    private var selectedAccountTypeValue = ""
    private var selectedAccountNumberValue = ""
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

        hideSystemBars()
        initialisers()
        listeners()

    }

    private fun hideSystemBars() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // Android 11 (API 30) and above
//            window.setDecorFitsSystemWindows(false)
//            window.insetsController?.let { controller ->
//                // Hide both status bar and navigation bar
//                controller.hide(WindowInsets.Type.systemBars())
//
//                // Optional: Make the system bars hide automatically
//                // This is similar to IMMERSIVE_STICKY behavior
//                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        } else {
//            // Fallback for older versions
//            @Suppress("DEPRECATION")
//            window.decorView.systemUiVisibility = (
//                    android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                            or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
//                            or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    )
//        }
    }

    private fun initialisers() {
//        fetchAssociatedAccountNumbers()
        fetchAssociatedAccountTypes()

        binding.actvShowAccountNumber.setOnItemClickListener { parent, view, position, id ->
            selectedAccountNumberValue = (parent.adapter as ShowAccountNumberAdapter).getItem(position).toString()
            Log.d("selectedAccountNumberTAG", "Selected account number: $selectedAccountNumberValue")
            binding.cvAccountInformation.visibility = View.INVISIBLE
            binding.progressBarGetResult.visibility = View.VISIBLE
            fetchInformation(selectedAccountTypeValue, selectedAccountNumberValue);
        }



    }

    private fun listeners() {

    }


    private fun fetchAssociatedAccountNumbers() {

        binding.cvAccountNumber.visibility = View.GONE
        binding.progressbarMain.visibility = View.VISIBLE

        ApiClient.getAccountDetailsInstance.getAccountDetails(
            "Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
            "ci_session=bfur80hklc6bks0jthl51vaojjq6dtfp",
            PrefsManager.getUserInformation(this@AccountOverviewActivity).data.memId
        ).enqueue(object : Callback<AccountsDetailResponse> {


            override fun onResponse(
                call: Call<AccountsDetailResponse?>,
                response: Response<AccountsDetailResponse?>
            ) {
                binding.cvAccountNumber.visibility = View.VISIBLE
                binding.progressbarMain.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d("fetchAssociatedAccountNumbersTAG", response.body()?.message.toString())
                    val s = response.body()
                    if (s?.status == 1) {
                        accounts.clear()
                        Log.d(
                            "fetchAssociatedAccountNumbersTAG",
                            "data size: " + response.body()?.data!!.size
                        )
                        for (i in 0 until s.data.size) {
                            accounts.add(
                                AccountDetailModel(
                                    s.data[i].idAuto,
                                    s.data[i].cid,
                                    s.data[i].accountNumberInternal,
                                    s.data[i].accountType,
                                    s.data[i].memberId,
                                    s.data[i].accountType,
                                    s.data[i].terminalId,
                                    s.data[i].subscriptionId,
                                    s.data[i].name,
                                    s.data[i].mobile,
                                    s.data[i].virtualAccount
                                )
                            )
                        }
                        makeAccountNumberSpinner()
                    }

                } else {
                    Log.d("fetchAssociatedAccountNumbersTAG", response.body()?.message.toString())
                    Toast.makeText(
                        this@AccountOverviewActivity,
                        "Incorrect Credentials",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AccountsDetailResponse?>, t: Throwable) {
                binding.cvAccountNumber.visibility = View.VISIBLE
                binding.progressbarMain.visibility = View.GONE
                Log.d("fetchAssociatedAccountNumbersTAG", "Error: ${t.message}")
                Toast.makeText(
                    this@AccountOverviewActivity,
                    "An error has occurred. Please try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


    }

    private fun fetchShowAccountNumbers(accType: String) {

        binding.cvAccountNumber.visibility = View.GONE
        binding.progressbarMain.visibility = View.VISIBLE

        ApiClient.getAccountNumber.getAccountNumber(accType).enqueue(object : Callback<ShowAccountNumberResponse> {


            override fun onResponse(call: Call<ShowAccountNumberResponse?>, response: Response<ShowAccountNumberResponse?>) {
                binding.cvAccountNumber.visibility = View.VISIBLE
                binding.progressbarMain.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d("fetchShowAccountNumbersTAG", response.body()?.status.toString())
                    val s = response.body()
                    if (s?.status == "200") {
                        if (s.data.isNotEmpty()) {
                            accounts.clear()
                            Log.d(
                                "fetchShowAccountNumbersTAG",
                                "data size: " + response.body()?.data!!.size
                            )
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
                            makeAccountNumberSpinner() // list show hogi, jo bhi us particular accountType ke andar aate ho......
                        }

                    }

                } else {
                    val gson = Gson();
                    Log.d("fetchShowAccountNumbersTAG", gson.toJson(response.body()))
                    Toast.makeText(
                        this@AccountOverviewActivity,
                        "Incorrect Credentials",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ShowAccountNumberResponse?>, t: Throwable) {
                binding.cvAccountNumber.visibility = View.VISIBLE
                binding.progressbarMain.visibility = View.GONE
                Log.d("fetchShowAccountNumbersTAG", "Error: ${t.message}")
                Toast.makeText(
                    this@AccountOverviewActivity,
                    "An error has occurred. Please try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun fetchAssociatedAccountTypes() {
        // ........ API call se fetch karne ke bad...........


        ApiClient.getTypesOfAccount.getTypesOfAccount()
            .enqueue(object : Callback<TypesOfAccountResponse> {


                override fun onResponse(
                    call: Call<TypesOfAccountResponse?>,
                    response: Response<TypesOfAccountResponse?>
                ) {
                    binding.cvAccountNumber.visibility = View.VISIBLE
                    binding.progressbarMain.visibility = View.GONE
                    if (response.isSuccessful) {
                        Log.d("fetchTypesOfAccountTAG", response.body()?.status.toString())
                        val s = response.body()
                        if (s?.status == "200") {
                            accountTypes.clear()
                            Log.d(
                                "fetchTypesOfAccountTAG",
                                "data size: " + response.body()?.data!!.size
                            )
                            for (i in 0 until s.data.size) {
                                accountTypes.add(
                                    TypesOfAccountsModel(
                                        s.data[i].Id,
                                        s.data[i].Name,
                                        s.data[i].Abr
                                    )
                                )
                            }
                            makeAccountTypeSpinner() // response milne ke bad, isi ki help se list prepare hogi...
                        }

                    } else {
                        val gson = Gson();
                        Log.d("fetchTypesOfAccountTAG", gson.toJson(response.body()))
                        Toast.makeText(
                            this@AccountOverviewActivity,
                            "Incorrect Credentials",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<TypesOfAccountResponse?>, t: Throwable) {
                    binding.cvAccountNumber.visibility = View.VISIBLE
                    binding.progressbarMain.visibility = View.GONE
                    Log.d("fetchAssociatedAccountNumbersTAG", "Error: ${t.message}")
                    Toast.makeText(
                        this@AccountOverviewActivity,
                        "An error has occurred. Please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


    }

    private fun makeAccountNumberSpinner() {


        val adapter = ShowAccountNumberAdapter(this, accountNumbersList)


        binding.actvShowAccountNumber.setAdapter(adapter)

//         Handle item selection
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

    private fun makeAccountTypeSpinner() {

        val adapter = ArrayAdapter(
            this,
            R.layout.custom_spinner_item,
            accountTypes.map { "${it.name}" }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }


        binding.spinnerAccountType.adapter = adapter

        binding.spinnerAccountType.setSelection(0)

        binding.spinnerAccountType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (!isUserSelecting) {
                        isUserSelecting = true
                        return
                    }

                    val selectedItem = parent?.getItemAtPosition(position).toString()

                    if (position == 0) {

                    } else {
                        // calling the API here.........
//                        binding.cvAccountInformation.visibility = View.INVISIBLE
//                        fetchInformation();
                        val selectedAccountType = accountTypes[position]
                        val selectedId = selectedAccountType.id
                        Log.d("selectedAccountTypeTAG", "Selected AccountTypeID: ${selectedId}");
                        selectedAccountTypeValue = selectedId.toString()
                        fetchShowAccountNumbers(selectedId) // fetch related accountNumber, based on the chosen accountType ID.........
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }

    private fun fetchInformation(selectedAccountType: String, selectedAccountNumber: String) {

        ApiClient.getAccountStatementDetail.getAccountStatementDetail(
            selectedAccountType,
            selectedAccountNumber,
            "2020-06-04",
            "2025-06-04"
        ).enqueue(object : Callback<AccountStatementDetailResponse> {


            override fun onResponse(
                call: Call<AccountStatementDetailResponse?>,
                response: Response<AccountStatementDetailResponse?>
            ) {
                val s: AccountStatementDetailResponse? = response.body();
                Log.d("fetchInformationTAG", "Status: " + s!!.status);
                val gson = Gson();
                binding.progressBarGetResult.visibility = View.GONE
                binding.cvAccountInformation.visibility = View.VISIBLE
                Log.d("fetchInformationTAG", "Body: " + gson.toJson(s.userDetail));

                // filling up the tabular data.......
                binding.tvApplicantName.text = s.userDetail.applicantName
                binding.tvAccountNumber.text = s.userDetail.accountNumber
                binding.tvBankName.text = s.userDetail.bankName
                binding.tvMemberId.text = s.userDetail.memberId
                binding.tvAccountType.text = s.userDetail.accountType
                binding.tvAccountOpeningDate.text = s.userDetail.accountOpeningDate
                binding.tvContactNo.text = s.userDetail.contactNo
                binding.tvVirtualAccount.text = s.userDetail.virtualAccount
                binding.tvFatherName.text = s.userDetail.fatherName
                binding.tvIFSCCode.text = s.userDetail.ifscCode
                binding.tvAddress.text = s.userDetail.address
                binding.tvPrintDate.text = s.userDetail.printDate


                binding.rvStatements.layoutManager = LinearLayoutManager(applicationContext)
                val statementAdapter = StatementAdapter(s.statementData)
                binding.rvStatements.adapter = statementAdapter
//                binding.tvAccountNumberValue.text = s.data.accNoInt
//                binding.tvTypeOfAccountValue.text = s.data.typeOfAcc
//                binding.tvMemIdValue.text = s.data.memId
//                binding.tvCifNoValue.text = s.data.cifNo
//                binding.tvNameValue.text = s.data.name
//                binding.tvFatherNameValue.text = s.data.father
//                binding.tvMobileNumberValue.text = s.data.mobile
//                binding.tvEmailValue.text = s.data.email
//                binding.tvPresentAddressValue.text = s.data.presentAddress
//                binding.tvPermanentAddressValue.text = s.data.permanentAddress
//                binding.tvPeriodValue.text = s.data.period
//                binding.tvEmiValue.text = s.data.emi
//                binding.tvMValueValue.text = s.data.mValue
//                binding.tvRate.text = s.data.rate
//                binding.tvMinimumBalanceValue.text = s.data.mbal
//                binding.tvMinimumBalanceChargeValue.text = s.data.mbalCharge
//                binding.tvRemarksValue.text = s.data.remarks
//                binding.tvNomineeValue.text = s.data.nominee
//                binding.tvNRelationValue.text = s.data.nRelation
//                binding.tvAgentValue.text = s.data.agent
//                binding.tvStatusValue.text = s.data.agent
//                binding.tvSanctionedLimitValue.text = s.data.sanctionedLimit
//                binding.tvSanctionedPeriodValue.text = s.data.sanctionedPeriod
//                binding.tvSanctionedDocumentValue.text = s.data.sDocument
//                binding.tvMDateValue.text = s.data.mDate
//                binding.tvDateNewValue.text = s.data.dateNew
//                binding.tvOtherInformationValue.text = s.data.otherInform
//                binding.tvIntrobyValue.text = s.data.introBy
//                binding.tvOperationModeValue.text = s.data.operationMode
//                binding.isSMSValue.text = s.data.isSMS
//                binding.isNBValue.text = s.data.isNB
//                binding.vAccountValue.text = s.data.vAccount
//                binding.cfTerminalIdValue.text = s.data.cfTerminalId
//                binding.tvSubenachValue.text = s.data.subEnach
//                binding.tvVAccountAubValue.text = s.data.vAccountAub
//                binding.cfSubscriptionIdValue.text = s.data.cfSubscriptionId
            }

            override fun onFailure(call: Call<AccountStatementDetailResponse?>, t: Throwable) {
                binding.progressBarGetResult.visibility = View.GONE
                Log.d("fetchInformationTAG", t.message.toString());
            }
        })
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