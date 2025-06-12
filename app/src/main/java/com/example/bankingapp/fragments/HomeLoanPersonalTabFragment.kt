package com.example.bankingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.bankingapp.R


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeLoanPersonalTabFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home_loan_personal_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerEmployment = view.findViewById<Spinner>(R.id.spinnerEmploymentType)
        val employmentTypes = arrayOf("Select Employment Type", "Salaried", "Self Employed", "Business Owner", "Professional")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, employmentTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEmployment.adapter = adapter

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeLoanPersonalTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}