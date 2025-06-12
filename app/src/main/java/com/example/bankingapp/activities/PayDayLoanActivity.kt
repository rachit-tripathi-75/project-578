package com.example.bankingapp.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.R
import com.example.bankingapp.databinding.ActivityPayDayLoanBinding

class PayDayLoanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayDayLoanBinding
    private var currentStep = 1
    private var loanAmount = 25000.0
    private val interestRate = 12.5
    private var tenure = 12

    // Form data
    private var monthlyIncome = ""
    private var fullName = ""
    private var email = ""
    private var phone = ""
    private var panCard = ""
    private var aadharCard = ""
    private var employmentType = ""
    private var companyName = ""
    private var workExperience = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayDayLoanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        setupSpinners()
        updateLoanSummary()
        showStep(1)


    }


    private fun setupListeners() {
        binding.seekBarLoanAmount.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                loanAmount = (progress * 1000 + 10000).toDouble()
                binding.tvLoanAmount.text = "₹${formatAmount(loanAmount.toInt())}"
                updateLoanSummary()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.btnContinue.setOnClickListener {
            when (currentStep) {
                1 -> {
                    if (validateStep1()) {
                        saveStep1Data()
                        showStep(2)
                    }
                }

                2 -> {
                    if (validateStep2()) {
                        saveStep2Data()
                        populateReviewData()
                        showStep(3)
                    }
                }

                3 -> {
                    if (validateStep3()) {
                        submitApplication()
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            when (currentStep) {
                2 -> showStep(1)
                3 -> showStep(2)
            }
        }

        binding.cbTermsAccepted.setOnCheckedChangeListener { _, isChecked ->
            binding.btnContinue.isEnabled = isChecked
        }
    }

    private fun setupSpinners() {
        // Tenure spinner
        val tenureOptions = arrayOf("6 Months", "12 Months", "18 Months", "24 Months", "36 Months")
        val tenureAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tenureOptions)
        tenureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTenure.adapter = tenureAdapter
        binding.spinnerTenure.setSelection(1) // Default to 12 months

        binding.spinnerTenure.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                tenure = when (position) {
                    0 -> 6
                    1 -> 12
                    2 -> 18
                    3 -> 24
                    4 -> 36
                    else -> 12
                }
                updateLoanSummary()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Employment type spinner
        val employmentOptions =
            arrayOf("Select Employment Type", "Salaried", "Self Employed", "Business Owner")
        val employmentAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, employmentOptions)
        employmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEmploymentType.adapter = employmentAdapter

        // Work experience spinner
        val experienceOptions =
            arrayOf("Select Experience", "0-1 Years", "1-3 Years", "3-5 Years", "5+ Years")
        val experienceAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, experienceOptions)
        experienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerWorkExperience.adapter = experienceAdapter
    }

    private fun showStep(step: Int) {
        currentStep = step

        // Hide all steps
        binding.step1Layout.visibility = View.GONE
        binding.step2Layout.visibility = View.GONE
        binding.step3Layout.visibility = View.GONE

        // Show current step
        when (step) {
            1 -> {
                binding.step1Layout.visibility = View.VISIBLE
                binding.btnBack.visibility = View.GONE
                binding.btnContinue.text = "Continue"
                binding.progressBar.progress = 33
                binding.tvProgressStep.text = "1/3"
            }

            2 -> {
                binding.step2Layout.visibility = View.VISIBLE
                binding.btnBack.visibility = View.VISIBLE
                binding.btnContinue.text = "Continue to Review"
                binding.progressBar.progress = 66
                binding.tvProgressStep.text = "2/3"
            }

            3 -> {
                binding.step3Layout.visibility = View.VISIBLE
                binding.btnBack.visibility = View.VISIBLE
                binding.btnContinue.text = "Submit Application"
                binding.btnContinue.isEnabled = binding.cbTermsAccepted.isChecked
                binding.progressBar.progress = 100
                binding.tvProgressStep.text = "3/3"
            }
        }
    }

    private fun validateStep1(): Boolean {
        monthlyIncome = binding.etMonthlyIncome.text.toString().trim()

        if (monthlyIncome.isEmpty()) {
            binding.etMonthlyIncome.error = "Please enter your monthly income"
            binding.etMonthlyIncome.requestFocus()
            return false
        }

        val income = monthlyIncome.toDoubleOrNull()
        if (income == null || income < 15000) {
            binding.etMonthlyIncome.error = "Minimum monthly income should be ₹15,000"
            binding.etMonthlyIncome.requestFocus()
            return false
        }

        return true
    }

    private fun validateStep2(): Boolean {
        fullName = binding.etFullName.text.toString().trim()
        email = binding.etEmail.text.toString().trim()
        phone = binding.etPhone.text.toString().trim()
        panCard = binding.etPanCard.text.toString().trim()
        aadharCard = binding.etAadharCard.text.toString().trim()
        companyName = binding.etCompanyName.text.toString().trim()

        if (fullName.isEmpty()) {
            binding.etFullName.error = "Please enter your full name"
            binding.etFullName.requestFocus()
            return false
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Please enter a valid email address"
            binding.etEmail.requestFocus()
            return false
        }

        if (phone.isEmpty() || phone.length != 10) {
            binding.etPhone.error = "Please enter a valid 10-digit phone number"
            binding.etPhone.requestFocus()
            return false
        }

        if (panCard.isEmpty() || panCard.length != 10) {
            binding.etPanCard.error = "Please enter a valid PAN card number"
            binding.etPanCard.requestFocus()
            return false
        }

        if (aadharCard.isEmpty() || aadharCard.length != 12) {
            binding.etAadharCard.error = "Please enter a valid 12-digit Aadhar number"
            binding.etAadharCard.requestFocus()
            return false
        }

        if (binding.spinnerEmploymentType.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select employment type", Toast.LENGTH_SHORT).show()
            return false
        }

        if (companyName.isEmpty()) {
            binding.etCompanyName.error = "Please enter company name"
            binding.etCompanyName.requestFocus()
            return false
        }

        if (binding.spinnerWorkExperience.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select work experience", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun validateStep3(): Boolean {
        if (!binding.cbTermsAccepted.isChecked) {
            Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun saveStep1Data() {
        monthlyIncome = binding.etMonthlyIncome.text.toString()
    }

    private fun saveStep2Data() {
        employmentType = binding.spinnerEmploymentType.selectedItem.toString()
        workExperience = binding.spinnerWorkExperience.selectedItem.toString()
    }

    private fun populateReviewData() {
        binding.tvReviewLoanAmount.text = "₹${formatAmount(loanAmount.toInt())}"
        binding.tvReviewTenure.text = "$tenure Months"

        val monthlyRate = interestRate / 100 / 12
        val emi = (loanAmount * monthlyRate * (1 + monthlyRate).pow(tenure)) /
                ((1 + monthlyRate).pow(tenure) - 1)
        binding.tvReviewEMI.text = "₹${formatAmount(emi.toInt())}"
        binding.tvReviewInterestRate.text = "$interestRate% p.a."

        binding.tvReviewFullName.text = fullName
        binding.tvReviewEmail.text = email
        binding.tvReviewPhone.text = phone
    }

    private fun updateLoanSummary() {
        val monthlyRate = interestRate / 100 / 12
        val emi = (loanAmount * monthlyRate * (1 + monthlyRate).pow(tenure)) /
                ((1 + monthlyRate).pow(tenure) - 1)
        val totalAmount = emi * tenure

        binding.tvSummaryLoanAmount.text = "₹${formatAmount(loanAmount.toInt())}"
        binding.tvMonthlyEMI.text = "₹${formatAmount(emi.toInt())}"
        binding.tvTotalAmount.text = "₹${formatAmount(totalAmount.toInt())}"
    }

    private fun formatAmount(amount: Int): String {
        return String.format("%,d", amount)
    }

    private fun submitApplication() {
        // Show loading state
        binding.btnContinue.isEnabled = false
        binding.btnContinue.text = "Submitting..."

        // Simulate API call with delay
        binding.btnContinue.postDelayed({
            // Reset button state
            binding.btnContinue.isEnabled = true
            binding.btnContinue.text = "Submit Application"

            // Show success message
            Toast.makeText(
                this,
                "Application submitted successfully! You will receive updates via SMS and email.",
                Toast.LENGTH_LONG
            ).show()

            // Here you would typically navigate to a success screen
            // For now, we'll reset to step 1
            resetForm()
        }, 2000)
    }

    private fun resetForm() {
        // Reset all form fields
        binding.etMonthlyIncome.text.clear()
        binding.etFullName.text.clear()
        binding.etEmail.text.clear()
        binding.etPhone.text.clear()
        binding.etPanCard.text.clear()
        binding.etAadharCard.text.clear()
        binding.etCompanyName.text.clear()
        binding.spinnerEmploymentType.setSelection(0)
        binding.spinnerWorkExperience.setSelection(0)
        binding.cbTermsAccepted.isChecked = false

        // Reset loan amount and tenure
        loanAmount = 25000.0
        tenure = 12
        binding.seekBarLoanAmount.progress = 15
        binding.spinnerTenure.setSelection(1)

        // Go back to step 1
        showStep(1)
        updateLoanSummary()
    }

    // Extension function to calculate power for Double
    private fun Double.pow(exponent: Int): Double {
//    return kotlin.math.pow(this, exponent.toDouble())
        return 0.0000
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}







