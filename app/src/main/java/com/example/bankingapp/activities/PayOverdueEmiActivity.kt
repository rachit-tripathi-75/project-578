package com.example.bankingapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.R
import com.example.bankingapp.databinding.ActivityPayOverdueEmiBinding

class PayOverdueEmiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayOverdueEmiBinding
    // Data
    private val loanDetails = LoanDetails(
        loanNumber = "SBL240001234",
        customerName = "Rajesh Kumar",
        loanAmount = 500000.0,
        outstandingAmount = 387500.0,
        overdueAmount = 15750.0,
        nextEMIAmount = 5250.0,
        nextEMIDue = "2024-01-15",
        lateFee = 525.0,
        totalOverdue = 16275.0,
        daysPastDue = 12,
        creditScore = 720
    )

    private val overdueEMIs = listOf(
        OverdueEMI(18, "2024-01-15", 5250.0, 262.50, 5512.50, 12),
        OverdueEMI(17, "2023-12-15", 5250.0, 262.50, 5512.50, 43)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayOverdueEmiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupData()
        setupListeners()

    }



    private fun setupData() {
        binding.tvAccountHolder.text = loanDetails.customerName
        binding.tvLoanAmount.text = "₹${formatAmount(loanDetails.loanAmount.toInt())}"
        binding.tvOutstanding.text = "₹${formatAmount(loanDetails.outstandingAmount.toInt())}"
        binding.tvTotalOverdue.text = "₹${formatAmount(loanDetails.totalOverdue.toInt())}"
        binding.tvFullAmount.text = "₹${formatAmount(loanDetails.totalOverdue.toInt())}"
        binding.tvSingleAmount.text = "₹${formatAmount(overdueEMIs[0].totalAmount.toInt())}"

        updateAmountToPay()
    }

    private fun setupListeners() {
        // Radio button listeners
        binding.rbFullPayment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.rbSinglePayment.isChecked = false
                binding.rbCustomPayment.isChecked = false
                binding.etCustomAmount.isEnabled = false
                updateAmountToPay()
            }
        }

        binding.rbSinglePayment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.rbFullPayment.isChecked = false
                binding.rbCustomPayment.isChecked = false
                binding.etCustomAmount.isEnabled = false
                updateAmountToPay()
            }
        }

        binding.rbCustomPayment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.rbFullPayment.isChecked = false
                binding.rbSinglePayment.isChecked = false
                binding.etCustomAmount.isEnabled = true
                binding.etCustomAmount.requestFocus()
                updateAmountToPay()
            } else {
                binding.etCustomAmount.isEnabled = false
            }
        }

        // Custom amount text watcher
        binding.etCustomAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && !binding.rbCustomPayment.isChecked) {
                binding.rbCustomPayment.isChecked = true
                binding.rbFullPayment.isChecked = false
                binding.rbSinglePayment.isChecked = false
            }
        }

        binding.etCustomAmount.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                if (binding.rbCustomPayment.isChecked) {
                    updateAmountToPay()
                }
            }
        })

        // Pay Now button
        binding.btnPayNow.setOnClickListener {
            val amount = calculateSelectedAmount()
            if (amount > 0) {
                proceedToPayment(amount)
            } else {
                Toast.makeText(this, "Please select a valid payment amount", Toast.LENGTH_SHORT).show()
            }
        }

        // Quick Action buttons
        binding.btnDownloadStatement.setOnClickListener {
            Toast.makeText(this, "Downloading statement...", Toast.LENGTH_SHORT).show()
            // Implement download functionality
        }

        binding.btnViewLoanDetails.setOnClickListener {
            Toast.makeText(this, "Opening loan details...", Toast.LENGTH_SHORT).show()
            // Navigate to loan details screen
        }

        binding.btnEMISchedule.setOnClickListener {
            Toast.makeText(this, "Opening EMI schedule...", Toast.LENGTH_SHORT).show()
            // Navigate to EMI schedule screen
        }

        binding.btnContactSupport.setOnClickListener {
            showContactOptions()
        }
    }

    private fun updateAmountToPay() {
        val amount = calculateSelectedAmount()
        binding.tvAmountToPay.text = "₹${formatAmount(amount.toInt())}"
        binding.btnPayNow.text = "Pay Now - ₹${formatAmount(amount.toInt())}"
        binding.btnPayNow.isEnabled = amount > 0
    }

    private fun calculateSelectedAmount(): Double {
        return when {
            binding.rbFullPayment.isChecked -> loanDetails.totalOverdue
            binding.rbSinglePayment.isChecked -> overdueEMIs[0].totalAmount
            binding.rbCustomPayment.isChecked -> {
                val customAmount = binding.etCustomAmount.text.toString().toDoubleOrNull() ?: 0.0
                if (customAmount >= overdueEMIs[0].totalAmount && customAmount <= loanDetails.totalOverdue) {
                    customAmount
                } else {
                    0.0
                }
            }
            else -> 0.0
        }
    }

    private fun proceedToPayment(amount: Double) {
        // Show payment method selection dialog or navigate to payment screen
        val intent = Intent(this, PaymentMethodActivity::class.java)
        intent.putExtra("PAYMENT_AMOUNT", amount)
        intent.putExtra("LOAN_NUMBER", loanDetails.loanNumber)
        startActivity(intent)
    }

    private fun showContactOptions() {
        val options = arrayOf("Call Customer Care", "Send Email", "WhatsApp Support")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Contact Support")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:18001234567")
                    startActivity(intent)
                }
                1 -> {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:loans@bank.com")
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Loan Support - ${loanDetails.loanNumber}")
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://wa.me/918001234567")
                    startActivity(intent)
                }
            }
        }
        builder.show()
    }

    private fun formatAmount(amount: Int): String {
        return String.format("%,d", amount)
    }

    // Data classes
    data class LoanDetails(
        val loanNumber: String,
        val customerName: String,
        val loanAmount: Double,
        val outstandingAmount: Double,
        val overdueAmount: Double,
        val nextEMIAmount: Double,
        val nextEMIDue: String,
        val lateFee: Double,
        val totalOverdue: Double,
        val daysPastDue: Int,
        val creditScore: Int
    )

    data class OverdueEMI(
        val emiNumber: Int,
        val dueDate: String,
        val amount: Double,
        val lateFee: Double,
        val totalAmount: Double,
        val daysPastDue: Int
    )


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}