package com.example.bankingapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.adapters.PaymentOptionAdapter
import com.example.bankingapp.classes.PaymentOption
import com.example.bankingapp.databinding.ActivityTransferMoneyBinding

class TransferMoneyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransferMoneyBinding
    private lateinit var adapter: PaymentOptionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTransferMoneyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()

    }

    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }


    }

    private fun setupRecyclerView() {
        val options = getPaymentOptions()
        adapter = PaymentOptionAdapter(options) { position ->
            handleOptionClick(position)
        }

        binding.optionsRecyclerView.apply {
            layoutManager = GridLayoutManager(this@TransferMoneyActivity, 2)
            adapter = this@TransferMoneyActivity.adapter

            // Apply layout animation
            val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(
                this@TransferMoneyActivity,
                R.anim.layout_animation
            )
            layoutAnimation = animation
            scheduleLayoutAnimation()
        }
    }

    private fun setupClickListeners() {
        binding.viewAccountButton.setOnClickListener {
            // Handle view account activity button click
            // For example: startActivity(Intent(this, AccountActivityActivity::class.java))
        }
    }

    private fun getPaymentOptions(): List<PaymentOption> {
        return listOf(
            PaymentOption("Send Money", R.drawable.ic_send),
            PaymentOption("Receive Money", R.drawable.ic_receive),
            PaymentOption("Scan Any QR", R.drawable.ic_qr_code),
            PaymentOption("BillPay / Recharge", R.drawable.ic_smartphone),
            PaymentOption("Manage Past Transfers", R.drawable.ic_arrow_left_right),
            PaymentOption("Beneficiaries UPI ID", R.drawable.ic_users)
        )
    }

    private fun handleOptionClick(position: Int) {
        when (position) {
            0 -> startActivity(Intent(this, UpiActivity::class.java))
            1 -> {
                // Handle Receive Money
            }
            2 -> {
                // Handle Scan QR
            }
            3 -> startActivity(Intent(this, BillPayAndRechargeActivity::class.java))
            4 -> {
                // Handle Past Transfers
            }
            5 -> {
                // Handle Beneficiaries
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}