package com.example.bankingapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.MainActivity
import com.example.bankingapp.R
import com.example.bankingapp.databinding.ActivityPaymentSuccessBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentSuccessBinding

    private var paymentAmount: Double = 0.0
    private var paymentMethod: String = ""
    private var paymentDetails: String = ""
    private var loanNumber: String = ""
    private var transactionId: String = ""
    private var transactionDate: String = ""
    private var transactionTime: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get data from intent
        getIntentData()
        setupData()
        setupListeners()
        // Generate current date and time
        generateTransactionDateTime()
    }

    private fun getIntentData() {
        paymentAmount = intent.getDoubleExtra("PAYMENT_AMOUNT", 0.0)
        paymentMethod = intent.getStringExtra("PAYMENT_METHOD") ?: ""
        paymentDetails = intent.getStringExtra("PAYMENT_DETAILS") ?: ""
        loanNumber = intent.getStringExtra("LOAN_NUMBER") ?: ""
        transactionId = intent.getStringExtra("TRANSACTION_ID") ?: ""
    }


    private fun setupData() {
        binding.tvTransactionId.text = transactionId
        binding.tvPaymentAmount.text = "₹${formatAmount(paymentAmount.toInt())}"
        binding.tvPaymentMethod.text = paymentMethod
        binding.tvPaymentDetails.text = paymentDetails
        binding.tvLoanNumber.text = loanNumber
        binding.tvTransactionDate.text = transactionDate
        binding.tvTransactionTime.text = transactionTime
        binding.tvStatus.text = "SUCCESS"
    }

    private fun generateTransactionDateTime() {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        transactionDate = dateFormat.format(currentDate)
        transactionTime = timeFormat.format(currentDate)

        binding.tvTransactionDate.text = transactionDate
        binding.tvTransactionTime.text = transactionTime
    }

    private fun setupListeners() {
        binding.btnDownloadReceipt.setOnClickListener {
            downloadReceipt()
        }

        binding.btnShareReceipt.setOnClickListener {
            shareReceipt()
        }

        binding.btnViewEMISchedule.setOnClickListener {
            viewEMISchedule()
        }

        binding.btnBackToHome.setOnClickListener {
            backToHome()
        }

        binding.btnContactSupport.setOnClickListener {
            contactSupport()
        }
    }

    private fun downloadReceipt() {
        Toast.makeText(this, "Receipt downloaded successfully", Toast.LENGTH_SHORT).show()
        // Implement actual download functionality here
        // You can generate a PDF receipt and save it to downloads folder
    }

    private fun shareReceipt() {
        val shareText = buildString {
            append("Payment Receipt\n")
            append("================\n\n")
            append("Transaction ID: $transactionId\n")
            append("Amount: ₹${formatAmount(paymentAmount.toInt())}\n")
            append("Payment Method: $paymentMethod\n")
            append("Loan Number: $loanNumber\n")
            append("Date: $transactionDate\n")
            append("Time: $transactionTime\n")
            append("Status: SUCCESS\n\n")
            append("Thank you for your payment!\n")
            append("QuickCash Payday Loans")
        }

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Payment Receipt - $transactionId")
        }

        startActivity(Intent.createChooser(shareIntent, "Share Receipt"))
    }

    private fun viewEMISchedule() {
        Toast.makeText(this, "Opening EMI Schedule...", Toast.LENGTH_SHORT).show()
        // Navigate to EMI Schedule Activity
        // val intent = Intent(this, EMIScheduleActivity::class.java)
        // intent.putExtra("LOAN_NUMBER", loanNumber)
        // startActivity(intent)
    }

    private fun backToHome() {
        // Clear the activity stack and go to main activity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun contactSupport() {
        val options = arrayOf("Call Customer Care", "Send Email", "WhatsApp Support", "Live Chat")
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
                    intent.data = Uri.parse("mailto:support@quickcash.com")
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Payment Support - $transactionId")
                    intent.putExtra(Intent.EXTRA_TEXT, "Transaction ID: $transactionId\nLoan Number: $loanNumber\n\nQuery: ")
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://wa.me/918001234567?text=Hi, I need help with transaction $transactionId")
                    startActivity(intent)
                }
                3 -> {
                    Toast.makeText(this, "Opening Live Chat...", Toast.LENGTH_SHORT).show()
                    // Implement live chat functionality
                }
            }
        }
        builder.show()
    }

    private fun formatAmount(amount: Int): String {
        return String.format("%,d", amount)
    }

    override fun onBackPressed() {
        // Override back button to go to home instead of previous activity
        super.onBackPressed()
        backToHome()
    }


}