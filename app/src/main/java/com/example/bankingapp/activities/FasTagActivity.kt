package com.example.bankingapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.adapters.FastTagTransactionAdapter
import com.example.bankingapp.databinding.ActivityFasTagBinding
import com.example.bankingapp.models.FastagTransactionModel

class FasTagActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFasTagBinding
    private var selectedVehicleType = "car"
    private var selectedAmount = 200
    private lateinit var transactionAdapter: FastTagTransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFasTagBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupViews()
        setupRecyclerView()
        setupClickListeners()

    }


    private fun setupViews() {
        // Set initial selected vehicle
        updateVehicleSelection("car")

        // Set initial selected amount
        updateAmountSelection(200)
    }

    private fun setupRecyclerView() {
        val transactions = listOf(
            FastagTransactionModel(1, 200, "Today, 2:30 PM", "Success", "Delhi-Gurgaon"),
            FastagTransactionModel(2, 150, "Yesterday, 8:45 AM", "Success", "Mumbai-Pune"),
            FastagTransactionModel(3, 300, "2 days ago", "Success", "Bangalore-Chennai")
        )

        transactionAdapter = FastTagTransactionAdapter(transactions)
        binding.rvTransactions.apply {
            layoutManager = LinearLayoutManager(this@FasTagActivity)
            adapter = transactionAdapter
        }
    }

    private fun setupClickListeners() {
        // Back button
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        // Vehicle selection
        binding.layoutCar.setOnClickListener {
            updateVehicleSelection("car")
        }

        binding.layoutTruck.setOnClickListener {
            updateVehicleSelection("truck")
        }

        binding.layoutBike.setOnClickListener {
            updateVehicleSelection("bike")
        }

        // Amount selection
        binding.amount100.setOnClickListener { updateAmountSelection(100) }
        binding.amount200.setOnClickListener { updateAmountSelection(200) }
        binding.amount500.setOnClickListener { updateAmountSelection(500) }
        binding.amount1000.setOnClickListener { updateAmountSelection(1000) }
        binding.amount2000.setOnClickListener { updateAmountSelection(2000) }
        binding.amount5000.setOnClickListener { updateAmountSelection(5000) }

        // Payment methods
        binding.layoutUPI.setOnClickListener {
            showPaymentMethodDialog("UPI")
        }

        binding.layoutCard.setOnClickListener {
            showPaymentMethodDialog("Card")
        }

        // View all transactions
        binding.btnViewAll.setOnClickListener {
            // Navigate to transactions history
            Toast.makeText(this, "Opening transaction history", Toast.LENGTH_SHORT).show()
        }

        // Recharge button
        binding.btnRecharge.setOnClickListener {
            processRecharge()
        }

        // Custom amount text change
        binding.etCustomAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                clearAmountSelection()
            }
        }
    }

    private fun updateVehicleSelection(vehicleType: String) {
        selectedVehicleType = vehicleType

        // Reset all backgrounds
        binding.layoutCar.background =
            ContextCompat.getDrawable(this, R.drawable.vehicle_selector_default)
        binding.layoutTruck.background =
            ContextCompat.getDrawable(this, R.drawable.vehicle_selector_default)
        binding.layoutBike.background =
            ContextCompat.getDrawable(this, R.drawable.vehicle_selector_default)

        // Set selected background
        when (vehicleType) {
            "car" -> binding.layoutCar.background =
                ContextCompat.getDrawable(this, R.drawable.vehicle_selector_selected)

            "truck" -> binding.layoutTruck.background =
                ContextCompat.getDrawable(this, R.drawable.vehicle_selector_selected)

            "bike" -> binding.layoutBike.background =
                ContextCompat.getDrawable(this, R.drawable.vehicle_selector_selected)
        }
    }

    private fun updateAmountSelection(amount: Int) {
        selectedAmount = amount
        binding.etCustomAmount.setText("")

        // Reset all amount backgrounds
        resetAmountBackgrounds()

        // Set selected background
        when (amount) {
            100 -> binding.amount100.background =
                ContextCompat.getDrawable(this, R.drawable.amount_selector_selected)

            200 -> binding.amount200.background =
                ContextCompat.getDrawable(this, R.drawable.amount_selector_selected)

            500 -> binding.amount500.background =
                ContextCompat.getDrawable(this, R.drawable.amount_selector_selected)

            1000 -> binding.amount1000.background =
                ContextCompat.getDrawable(this, R.drawable.amount_selector_selected)

            2000 -> binding.amount2000.background =
                ContextCompat.getDrawable(this, R.drawable.amount_selector_selected)

            5000 -> binding.amount5000.background =
                ContextCompat.getDrawable(this, R.drawable.amount_selector_selected)
        }

        updateRechargeButton()
    }

    private fun clearAmountSelection() {
        resetAmountBackgrounds()
        updateRechargeButton()
    }

    private fun resetAmountBackgrounds() {
        binding.amount100.background =
            ContextCompat.getDrawable(this, R.drawable.amount_selector_default)
        binding.amount200.background =
            ContextCompat.getDrawable(this, R.drawable.amount_selector_default)
        binding.amount500.background =
            ContextCompat.getDrawable(this, R.drawable.amount_selector_default)
        binding.amount1000.background =
            ContextCompat.getDrawable(this, R.drawable.amount_selector_default)
        binding.amount2000.background =
            ContextCompat.getDrawable(this, R.drawable.amount_selector_default)
        binding.amount5000.background =
            ContextCompat.getDrawable(this, R.drawable.amount_selector_default)
    }

    private fun updateRechargeButton() {
        val customAmount = binding.etCustomAmount.text.toString()
        val displayAmount = if (customAmount.isNotEmpty()) {
            customAmount
        } else {
            selectedAmount.toString()
        }

        binding.btnRecharge.text = "Recharge ₹$displayAmount"
    }

    private fun showPaymentMethodDialog(method: String) {
        Toast.makeText(this, "Selected payment method: $method", Toast.LENGTH_SHORT).show()
    }

    private fun processRecharge() {
        val customAmount = binding.etCustomAmount.text.toString()
        val rechargeAmount = if (customAmount.isNotEmpty()) {
            customAmount.toIntOrNull() ?: 0
        } else {
            selectedAmount
        }

        if (rechargeAmount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading and process recharge
        Toast.makeText(
            this,
            "Processing recharge of ₹$rechargeAmount for $selectedVehicleType",
            Toast.LENGTH_LONG
        ).show()

        // Here you would typically call your payment gateway API
        // For demo purposes, we'll just show a success message
        processPayment(rechargeAmount)
    }

    private fun processPayment(amount: Int) {
        // Simulate payment processing
        binding.btnRecharge.isEnabled = false
        binding.btnRecharge.text = "Processing..."

        // Simulate API call delay
        binding.btnRecharge.postDelayed({
            binding.btnRecharge.isEnabled = true
            updateRechargeButton()
            Toast.makeText(
                this,
                "Recharge successful! ₹$amount added to your FASTag",
                Toast.LENGTH_LONG
            ).show()

            // Update balance (in real app, this would come from API response)
            updateBalance(amount)
        }, 2000)
    }

    private fun updateBalance(rechargeAmount: Int) {
        val currentBalance = 1250 // This would come from your data source
        val newBalance = currentBalance + rechargeAmount
        binding.tvBalance.text = "₹${String.format("%,.2f", newBalance.toDouble())}"
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


}