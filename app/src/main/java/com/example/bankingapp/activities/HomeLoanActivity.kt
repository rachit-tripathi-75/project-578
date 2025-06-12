package com.example.bankingapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.R
import com.example.bankingapp.adapters.HomeLoanPagerAdapter
import com.example.bankingapp.databinding.ActivityHomeLoanBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

class HomeLoanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeLoanBinding


    private val numberFormat = NumberFormat.getNumberInstance(Locale("en", "IN"))
    private val interestRate = 8.5 // will be changed according to the requirement..........


    private var propertyValue = 5000000 // 50 lakhs
    private var loanAmount = 4000000 // 40 lakhs
    private var tenure = 240 // 20 years in months

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeLoanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSeekBars()
        setupTabs()
        setupClickListeners()
        calculateEMI()

    }


    private fun setupSeekBars() {
        // Property Value SeekBar (10L to 10Cr)
        binding.seekBarPropertyValue.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                propertyValue =
                    1000000 + (progress * 990000) // its in the range of 10L to 10Cr range.......
                binding.tvPropertyValue.text = "Property Value: ₹${formatAmount(propertyValue)}"
                updateLoanAmountLimits()
                calculateEMI()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Loan Amount SeekBar (10% to 90% of property value)
        binding.seekBarLoanAmount.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val minLoan = (propertyValue * 0.1).toInt()
                val maxLoan = (propertyValue * 0.9).toInt()
                loanAmount = minLoan + ((maxLoan - minLoan) * progress / 90)
                binding.tvLoanAmount.text = "Loan Amount: ₹${formatAmount(loanAmount)}"
                calculateEMI()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Tenure SeekBar (5 to 30 years)
        binding.seekBarTenure.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tenure = (5 + progress) * 12 // 5 to 30 years in months
                binding.tvTenure.text = "Loan Tenure: ${tenure / 12} years"
                calculateEMI()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun updateLoanAmountLimits() {
        val minLoan = (propertyValue * 0.1).toInt()
        val maxLoan = (propertyValue * 0.9).toInt()
        binding.tvLoanAmountMin.text = "₹${formatAmount(minLoan)}"
        binding.tvLoanAmountMax.text = "₹${formatAmount(maxLoan)}"

        // Update loan amount if it's outside the new limits
        if (loanAmount < minLoan) {
            loanAmount = minLoan
            binding.seekBarLoanAmount.progress = 10
        } else if (loanAmount > maxLoan) {
            loanAmount = maxLoan
            binding.seekBarLoanAmount.progress = 90
        }
        binding.tvLoanAmount.text = "Loan Amount: ₹${formatAmount(loanAmount)}"
    }

    private fun setupTabs() {
        val adapter = HomeLoanPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Personal"
                1 -> "Property"
                2 -> "Documents"
                else -> ""
            }
        }.attach()
    }

    private fun setupClickListeners() {
        binding.btnApplyNow.setOnClickListener {
            submitApplication()
        }

//        findViewById<Button>(R.id.btnCallExpert).setOnClickListener {
//            Toast.makeText(this, "Calling Home Loan Expert...", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun calculateEMI() {
        val monthlyRate = interestRate / 12 / 100
        val emi = (loanAmount * monthlyRate * (1 + monthlyRate).pow(tenure)) /
                ((1 + monthlyRate).pow(tenure) - 1)

        val totalAmount = emi * tenure
        val totalInterest = totalAmount - loanAmount
        val downPayment = propertyValue - loanAmount
        val downPaymentPercentage = ((downPayment.toDouble() / propertyValue) * 100).toInt()

        binding.tvEMI.text = "₹${numberFormat.format(emi.toInt())}"
        binding.tvTotalInterest.text = "₹${formatAmount(totalInterest.toInt())}"
        binding.tvDownPayment.text = "₹${formatAmount(downPayment)} (${downPaymentPercentage}%)"
    }

    private fun formatAmount(amount: Int): String {
        return when {
            amount >= 10000000 -> "${amount / 10000000}Cr"
            amount >= 100000 -> "${amount / 100000}L"
            else -> numberFormat.format(amount)
        }
    }

    private fun submitApplication() {
        Toast.makeText(
            this,
            "Home Loan Application submitted successfully! Our expert will contact you shortly.",
            Toast.LENGTH_LONG
        ).show()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


}





