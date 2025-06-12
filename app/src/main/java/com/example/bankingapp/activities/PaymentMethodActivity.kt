package com.example.bankingapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.R
import com.example.bankingapp.databinding.ActivityPaymentMethodBinding
import com.google.android.material.tabs.TabLayout

class PaymentMethodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentMethodBinding
    private var paymentAmount: Double = 0.0
    private var loanNumber: String = ""
    private var selectedPaymentMethod: String = "netbanking"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupData()
        setupListeners()
        setupBankSpinner()
        // Show default tab (Net Banking)
        showPaymentMethod("netbanking")

    }


    private fun setupData() {
        binding.tvPaymentAmount.text = "₹${formatAmount(paymentAmount.toInt())}"
        binding.tvLoanNumber.text = "Loan: $loanNumber"
    }

    private fun setupListeners() {
        // Tab selection listener
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showPaymentMethod("netbanking")
                    1 -> showPaymentMethod("upi")
                    2 -> showPaymentMethod("card")
                    3 -> showPaymentMethod("wallet")
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Net Banking
        binding.btnNetBanking.setOnClickListener {
            val selectedBank = binding.spinnerBank.selectedItem.toString()
            if (selectedBank != "Select Bank") {
                processPayment("Net Banking", selectedBank)
            } else {
                Toast.makeText(this, "Please select a bank", Toast.LENGTH_SHORT).show()
            }
        }

        // UPI Buttons
        binding.btnGooglePay.setOnClickListener {
            processUpiPayment("Google Pay")
        }

        binding.btnPhonePe.setOnClickListener {
            processUpiPayment("PhonePe")
        }

        binding.btnPaytm.setOnClickListener {
            processUpiPayment("Paytm")
        }

        binding.btnUpiPay.setOnClickListener {
            val upiId = binding.etUpiId.text.toString().trim()
            if (upiId.isNotEmpty() && isValidUpiId(upiId)) {
                processPayment("UPI", upiId)
            } else {
                Toast.makeText(this, "Please enter a valid UPI ID", Toast.LENGTH_SHORT).show()
            }
        }

        // Card Payment
        binding.btnCardPay.setOnClickListener {
            if (validateCardDetails()) {
                processPayment("Card", "****${binding.etCardNumber.text.toString().takeLast(4)}")
            }
        }

        // Wallet Buttons
        binding.btnPaytmWallet.setOnClickListener {
            processPayment("Paytm Wallet", "Paytm")
        }

        binding.btnAmazonPay.setOnClickListener {
            processPayment("Amazon Pay", "Amazon")
        }

        binding.btnMobikwik.setOnClickListener {
            processPayment("Mobikwik", "Mobikwik")
        }

        // Card input formatters
        setupCardInputFormatters()
    }

    private fun setupBankSpinner() {
        val banks = arrayOf(
            "Select Bank",
            "State Bank of India",
            "HDFC Bank",
            "ICICI Bank",
            "Axis Bank",
            "Kotak Mahindra Bank",
            "Punjab National Bank",
            "Bank of Baroda",
            "Canara Bank",
            "Union Bank of India",
            "Indian Bank"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, banks)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBank.adapter = adapter
    }

    private fun showPaymentMethod(method: String) {
        selectedPaymentMethod = method

        // Hide all layouts
        binding.netBankingLayout.visibility = View.GONE
        binding.upiLayout.visibility = View.GONE
        binding.cardLayout.visibility = View.GONE
        binding.walletLayout.visibility = View.GONE

        // Show selected layout
        when (method) {
            "netbanking" -> binding.netBankingLayout.visibility = View.VISIBLE
            "upi" -> binding.upiLayout.visibility = View.VISIBLE
            "card" -> binding.cardLayout.visibility = View.VISIBLE
            "wallet" -> binding.walletLayout.visibility = View.VISIBLE
        }
    }

    private fun setupCardInputFormatters() {
        // Card number formatter (add spaces every 4 digits)
        binding.etCardNumber.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                isFormatting = true
                val text = s.toString().replace(" ", "")
                val formatted = StringBuilder()

                for (i in text.indices) {
                    if (i > 0 && i % 4 == 0) {
                        formatted.append(" ")
                    }
                    formatted.append(text[i])
                }

                binding.etCardNumber.setText(formatted.toString())
                binding.etCardNumber.setSelection(formatted.length)
                isFormatting = false
            }
        })

        // Expiry date formatter (MM/YY)
        binding.etExpiryDate.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                isFormatting = true
                val text = s.toString().replace("/", "")
                val formatted = StringBuilder()

                for (i in text.indices) {
                    if (i == 2) {
                        formatted.append("/")
                    }
                    formatted.append(text[i])
                }

                binding.etExpiryDate.setText(formatted.toString())
                binding.etExpiryDate.setSelection(formatted.length)
                isFormatting = false
            }
        })
    }

    private fun validateCardDetails(): Boolean {
        val cardNumber = binding.etCardNumber.text.toString().replace(" ", "")
        val cardName = binding.etCardName.text.toString().trim()
        val expiryDate = binding.etExpiryDate.text.toString().trim()
        val cvv = binding.etCvv.text.toString().trim()

        when {
            cardNumber.length < 16 -> {
                Toast.makeText(this, "Please enter a valid card number", Toast.LENGTH_SHORT).show()
                return false
            }
            cardName.isEmpty() -> {
                Toast.makeText(this, "Please enter cardholder name", Toast.LENGTH_SHORT).show()
                return false
            }
            expiryDate.length != 5 -> {
                Toast.makeText(this, "Please enter valid expiry date (MM/YY)", Toast.LENGTH_SHORT).show()
                return false
            }
            cvv.length < 3 -> {
                Toast.makeText(this, "Please enter valid CVV", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun isValidUpiId(upiId: String): Boolean {
        return upiId.contains("@") && upiId.length > 5
    }

    private fun processUpiPayment(app: String) {
        when (app) {
            "Google Pay" -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("upi://pay?pa=merchant@upi&pn=QuickCash&am=${paymentAmount}&cu=INR&tn=EMI Payment $loanNumber")
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Google Pay not installed", Toast.LENGTH_SHORT).show()
                }
            }
            "PhonePe" -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("phonepe://pay?pa=merchant@upi&pn=QuickCash&am=${paymentAmount}&cu=INR&tn=EMI Payment $loanNumber")
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "PhonePe not installed", Toast.LENGTH_SHORT).show()
                }
            }
            "Paytm" -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("paytmmp://pay?pa=merchant@upi&pn=QuickCash&am=${paymentAmount}&cu=INR&tn=EMI Payment $loanNumber")
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Paytm not installed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun processPayment(method: String, details: String) {
        // Show loading dialog
        val loadingDialog = android.app.ProgressDialog(this)
        loadingDialog.setMessage("Processing payment...")
        loadingDialog.setCancelable(false)
        loadingDialog.show()

        // Simulate payment processing
        android.os.Handler().postDelayed({
            loadingDialog.dismiss()

            // Navigate to payment success screen
            val intent = Intent(this, PaymentSuccessActivity::class.java)
            intent.putExtra("PAYMENT_AMOUNT", paymentAmount)
            intent.putExtra("PAYMENT_METHOD", method)
            intent.putExtra("PAYMENT_DETAILS", details)
            intent.putExtra("LOAN_NUMBER", loanNumber)
            intent.putExtra("TRANSACTION_ID", generateTransactionId())
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun generateTransactionId(): String {
        return "TXN${System.currentTimeMillis()}"
    }

    private fun formatAmount(amount: Int): String {
        return String.format("%,d", amount)
    }


}