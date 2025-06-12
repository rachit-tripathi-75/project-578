package com.example.bankingapp.activities

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.DatePicker
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.R
import com.example.bankingapp.activities.PersonalLoanActivity.LoanApplication
import com.example.bankingapp.classes.TwoWheelerLoanApplication
import com.example.bankingapp.databinding.ActivityTwoWheelerLoanBinding
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.pow


class TwoWheelerLoanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTwoWheelerLoanBinding
    private var loanAmount = 150000 // Default loan amount
    private var tenure = 24 // Default tenure in months
    private var interestRate = 12.0 // Annual interest rate

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTwoWheelerLoanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        initializeViews();
        setupSeekBars();
        setupDatePicker();
        setupValidation();
        calculateEMI();

    }


    private fun initializeViews() {

        binding.btnApplyLoan.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (validateForm()) {
                    submitLoanApplication()
                }
            }
        })
    }

    private fun setupSeekBars() {
        // Loan Amount SeekBar (50K to 5L)
        binding.seekBarLoanAmount.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                loanAmount = 50000 + (progress * 1000) // Min 50K, increment by 1K
                binding.tvLoanAmount.setText("₹" + formatAmount(loanAmount))
                calculateEMI()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Tenure SeekBar (12 to 60 months)
        binding.seekBarTenure.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tenure = 12 + progress // Min 12 months
                binding.tvTenure.setText(tenure.toString() + "M")
                calculateEMI()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupDatePicker() {
        binding.etDOB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val datePickerDialog = DatePickerDialog(
                    this@TwoWheelerLoanActivity,
                    object : OnDateSetListener {
                        override fun onDateSet(
                            view: DatePicker?,
                            year: Int,
                            month: Int,
                            dayOfMonth: Int
                        ) {
                            calendar?.set(Calendar.YEAR, year)
                            calendar?.set(Calendar.MONTH, month)
                            calendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                            val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            binding.etDOB.setText(sdf.format(calendar.getTime()))
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )


                // Set max date to 18 years ago
                val maxDate: Calendar = Calendar.getInstance()
                maxDate.add(Calendar.YEAR, -18)
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis())

                datePickerDialog.show()
            }
        })
    }

    private fun setupValidation() {
        // PAN validation
        binding.etPAN.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val pan = s.toString().uppercase(Locale.getDefault())
                if (pan.length == 10) {
                    if (isValidPAN(pan)) {
                        binding.etPAN.setError(null)
                    } else {
                        binding.etPAN.setError("Invalid PAN format")
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Mobile validation
        binding.etMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val mobile = s.toString()
                if (mobile.length == 10) {
                    if (isValidMobile(mobile)) {
                        binding.etMobile.setError(null)
                    } else {
                        binding.etMobile.setError("Invalid mobile number")
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun calculateEMI() {
        val monthlyRate = interestRate / 100 / 12
        val emi = (loanAmount * monthlyRate * (1 + monthlyRate).pow(tenure.toDouble())) /
                ((1 + monthlyRate).pow(tenure.toDouble()) - 1)

        val df: DecimalFormat = DecimalFormat("#,##,###")
        binding.tvEMI.setText("₹" + df.format(Math.round(emi)))
    }

    private fun formatAmount(amount: Int): String {
        if (amount >= 100000) {
            return (amount / 1000).toString() + "K"
        } else {
            return amount.toString()
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (binding.etFirstName.getText().toString().trim().isEmpty()) {
            binding.etFirstName.setError("First name is required")
            isValid = false
        }

        if (binding.etLastName.getText().toString().trim().isEmpty()) {
            binding.etLastName.setError("Last name is required")
            isValid = false
        }

        val mobile = binding.etMobile.getText().toString().trim()
        if (mobile.isEmpty() || !isValidMobile(mobile)) {
            binding.etMobile.setError("Valid mobile number is required")
            isValid = false
        }

        val email = binding.etEmail.getText().toString().trim()
        if (email.isEmpty() || !isValidEmail(email)) {
            binding.etEmail.setError("Valid email is required")
            isValid = false
        }

        if (binding.etDOB.getText().toString().trim().isEmpty()) {
            binding.etDOB.setError("Date of birth is required")
            isValid = false
        }

        val pan = binding.etPAN.getText().toString().trim()
        if (pan.isEmpty() || !isValidPAN(pan)) {
            binding.etPAN.setError("Valid PAN number is required")
            isValid = false
        }

        return isValid
    }

    private fun isValidMobile(mobile: String): Boolean {
        return mobile.matches("^[6-9]\\d{9}$".toRegex())
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPAN(pan: String): Boolean {
        return pan.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$".toRegex())
    }

    private fun submitLoanApplication() {
        // Create loan application object
        val application = TwoWheelerLoanApplication()
        application.firstName = binding.etFirstName.getText().toString().trim()
        application.lastName = binding.etLastName.getText().toString().trim()
        application.mobile = binding.etMobile.getText().toString().trim()
        application.email = binding.etEmail.getText().toString().trim()
        application.dateOfBirth = binding.etDOB.getText().toString().trim()
        application.panNumber = binding.etPAN.getText().toString().trim().toUpperCase()
        application.loanAmount = loanAmount
        application.tenure = tenure
        application.interestRate = interestRate


        // Calculate EMI
        val monthlyRate = interestRate / 100 / 12
        val emi = (loanAmount * monthlyRate * (1 + monthlyRate).pow(tenure.toDouble())) /
                ((1 + monthlyRate).pow(tenure.toDouble()) - 1)
        application.emi = Math.round(emi)

        // Here you would typically send the application to your backend API
        // For demo purposes, we'll just show a success message
        Toast.makeText(this, "Loan application submitted successfully!", Toast.LENGTH_LONG).show()


        // You can also save to local database or send to server
        // saveToDatabase(application);
        // sendToServer(application);
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}