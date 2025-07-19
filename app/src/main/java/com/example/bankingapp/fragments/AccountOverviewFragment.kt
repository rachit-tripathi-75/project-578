package com.example.bankingapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.adapters.StatementAdapter
import com.example.bankingapp.classes.ApiClient
import com.example.bankingapp.databinding.FragmentAccountOverviewBinding
import com.example.bankingapp.responses.AccountStatementDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AccountOverviewFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAccountOverviewBinding? = null
    private val binding get() = _binding!!

    private var accountType: String? = null
    private var accountNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            accountType = it.getString(ARG_ACCOUNT_TYPE)
            accountNumber = it.getString(ARG_ACCOUNT_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (accountType != null && accountNumber != null) {
            fetchAccountDetails(accountType!!, accountNumber!!)
        }
    }


    private fun fetchAccountDetails(selectedAccountType: String, selectedAccountNumber: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.scrollView.visibility = View.GONE

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
                binding.progressBar.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE

                val s: AccountStatementDetailResponse? = response.body()
                if (s != null) {
                    populateAccountDetails(s)
                }
            }

            override fun onFailure(call: Call<AccountStatementDetailResponse?>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.d("AccountOverviewFragment", "Error: ${t.message}")
            }
        })
    }

    private fun populateAccountDetails(response: AccountStatementDetailResponse) {
        // Populate account information
        binding.tvMemberIdValue.text = response.userDetail.memberId
        binding.tvAccountTypeValue.text = response.userDetail.accountType
        binding.tvAccountNumberValue.text = response.userDetail.accountNumber
        binding.tvAccountOpeningDateValue.text = response.userDetail.accountOpeningDate
        binding.tvApplicantNameValue.text = response.userDetail.applicantName
        binding.tvContactNoValue.text = response.userDetail.contactNo
        binding.tvVirtualAccountValue.text = response.userDetail.virtualAccount
        binding.tvFatherNameValue.text = response.userDetail.fatherName
        binding.tvIFSCCodeValue.text = response.userDetail.ifscCode
        binding.tvAddressValue.text = response.userDetail.address
        binding.tvBankNameValue.text = response.userDetail.bankName
        binding.tvPrintDateValue.text = response.userDetail.printDate

        // Setup RecyclerView for statements
        binding.rvStatements.layoutManager = LinearLayoutManager(requireContext())
        val statementAdapter = StatementAdapter(response.statementData)
        binding.rvStatements.adapter = statementAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val ARG_ACCOUNT_TYPE = "account_type"
        private const val ARG_ACCOUNT_NUMBER = "account_number"

        fun newInstance(accountType: String, accountNumber: String): AccountOverviewFragment {
            val fragment = AccountOverviewFragment()
            val args = Bundle()
            args.putString(ARG_ACCOUNT_TYPE, accountType)
            args.putString(ARG_ACCOUNT_NUMBER, accountNumber)
            fragment.arguments = args
            return fragment
        }
    }
}