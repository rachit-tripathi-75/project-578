package com.example.bankingapp.activities

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.R
import com.example.bankingapp.adapters.PersonalLoanFormViewPagerAdapter
import com.example.bankingapp.databinding.ActivityPersonalLoanBinding
import com.example.bankingapp.databinding.PersonalLoanFormBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

class PersonalLoanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalLoanBinding
    private lateinit var bindingPersonalLoanForm: PersonalLoanFormBinding


    private val numberFormat = NumberFormat.getNumberInstance(Locale("en", "IN"))
    private val interestRate = 10.5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPersonalLoanBinding.inflate(layoutInflater)
        bindingPersonalLoanForm = binding.layoutPersonalLoanForm
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()

    }

    private fun initialisers() {
        initViews()
        setupSeekBars()
        setupApplicationFormViewPager()
        setupSpinners()
        setupClickListeners()
        calculateEMI()
    }


    private fun initViews() {

    }

    private fun setupApplicationFormViewPager() {

        val adapter = PersonalLoanFormViewPagerAdapter(this)
        bindingPersonalLoanForm.viewPager.adapter = adapter

        TabLayoutMediator(
            bindingPersonalLoanForm.tabLayout,
            bindingPersonalLoanForm.viewPager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> "Applicant"
                1 -> "Co-Applicant"
                else -> null
            }
        }.attach()
    }


    private fun setupSeekBars() {
        // Loan Amount SeekBar (50K to 50L)
        binding.seekBarLoanAmount.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val loanAmount = 50000 + (progress * 49500) // 50K to 50L range
                binding.tvLoanAmount.text = "Loan Amount: ₹${numberFormat.format(loanAmount)}"
                calculateEMI()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Tenure SeekBar (6 to 84 months)
        binding.seekBarTenure.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val tenure = 6 + (progress * 6) // 6 to 84 months in steps of 6
                binding.tvTenure.text = "Tenure: $tenure months"
                calculateEMI()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupSpinners() {
//        // Employment Type Spinner
//        val employmentTypes =
//            arrayOf("Select Employment Type", "Salaried", "Self Employed", "Business Owner")
//        val employmentAdapter =
//            ArrayAdapter(this, android.R.layout.simple_spinner_item, employmentTypes)
//        employmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerEmployment.adapter = employmentAdapter
//
//        // City Spinner
//        val cities =
//            arrayOf("Select City", "Mumbai", "Delhi", "Bangalore", "Pune", "Hyderabad", "Chennai")
//        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
//        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerCity.adapter = cityAdapter
    }

    private fun setupClickListeners() {
//        binding.btnApplyNow.setOnClickListener {
//            if (validateForm()) {
//                submitApplication()
//            }
//        }

        findViewById<Button>(R.id.btnCallNow).setOnClickListener {
            // Handle call now action
            Toast.makeText(this, "Calling 1800-123-4567...", Toast.LENGTH_SHORT).show()
            Log.d("callConfirmationTAG", "calling will be held at this number")

            // at the time of call picking up....... will redirect to the dialer menu with the text of phone number as provided.......
        }
    }

    private fun calculateEMI() {
        val loanAmount = 50000 + (binding.seekBarLoanAmount.progress * 49500).toDouble()
        val tenure = 6 + (binding.seekBarTenure.progress * 6)
        val monthlyRate = interestRate / 12 / 100

        val emi = (loanAmount * monthlyRate * (1 + monthlyRate).pow(tenure)) /
                ((1 + monthlyRate).pow(tenure) - 1)

        val totalAmount = emi * tenure
        val totalInterest = totalAmount - loanAmount

        binding.tvEMI.text = "₹${numberFormat.format(emi.toInt())}"
        binding.tvTotalInterest.text = "₹${numberFormat.format(totalInterest.toInt())}"
        binding.tvTotalAmount.text = "₹${numberFormat.format(totalAmount.toInt())}"
    }

    private fun validateForm(): Boolean {
        var isValid = true
//
//        if (binding.etFullName.text.toString().trim().isEmpty()) {
//            binding.etFullName.error = "Please enter your full name"
//            isValid = false
//        }
//
//        if (binding.etEmail.text.toString().trim().isEmpty()) {
//            binding.etEmail.error = "Please enter your email"
//            isValid = false
//        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString())
//                .matches()
//        ) {
//            binding.etEmail.error = "Please enter a valid email"
//            isValid = false
//        }
//
//        if (binding.etPhone.text.toString().trim().isEmpty()) {
//            binding.etPhone.error = "Please enter your mobile number"
//            isValid = false
//        } else if (binding.etPhone.text.toString().length != 10) {
//            binding.etPhone.error = "Please enter a valid 10-digit mobile number"
//            isValid = false
//        }
//
//        if (binding.etIncome.text.toString().trim().isEmpty()) {
//            binding.etIncome.error = "Please enter your monthly income"
//            isValid = false
//        }
//
//        if (binding.spinnerEmployment.selectedItemPosition == 0) {
//            Toast.makeText(this, "Please select employment type", Toast.LENGTH_SHORT).show()
//            isValid = false
//        }
//
//        if (binding.spinnerCity.selectedItemPosition == 0) {
//            Toast.makeText(this, "Please select your city", Toast.LENGTH_SHORT).show()
//            isValid = false
//        }
//
        return isValid
    }

    private fun submitApplication() {
        // Create loan application data
//        val loanApplication = LoanApplication(
//            fullName = binding.etFullName.text.toString().trim(),
//            email = binding.etEmail.text.toString().trim(),
//            phone = binding.etPhone.text.toString().trim(),
//            monthlyIncome = binding.etIncome.text.toString().trim().toDoubleOrNull() ?: 0.0,
//            employmentType = binding.spinnerEmployment.selectedItem.toString(),
//            city = binding.spinnerCity.selectedItem.toString(),
//            loanAmount = 50000 + (binding.seekBarLoanAmount.progress * 49500),
//            tenure = 6 + (binding.seekBarTenure.progress * 6)
//        )
//
//        // Show success message
//        Toast.makeText(
//            this,
//            "Application submitted successfully! You will receive a confirmation shortly.",
//            Toast.LENGTH_LONG
//        ).show()
//
//        // Clear form
//        clearForm()
    }

    private fun clearForm() {
//        binding.etFullName.text?.clear()
//        binding.etEmail.text?.clear()
//        binding.etPhone.text?.clear()
//        binding.etIncome.text?.clear()
//        binding.spinnerEmployment.setSelection(0)
//        binding.spinnerCity.setSelection(0)
    }


    data class LoanApplication(
        val fullName: String,
        val email: String,
        val phone: String,
        val monthlyIncome: Double,
        val employmentType: String,
        val city: String,
        val loanAmount: Int,
        val tenure: Int
    )


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}