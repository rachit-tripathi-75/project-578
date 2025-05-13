package com.example.bankingapp.activities

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.adapters.UpiOptionAdapter
import com.example.bankingapp.classes.UpiOption
import com.example.bankingapp.databinding.ActivityUpiBinding

class UpiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpiBinding
    private lateinit var adapter: UpiOptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        setupRecyclerView()

    }

    private fun setupToolbar() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }


    }

    private fun setupRecyclerView() {
        val options = getUpiOptions()
        adapter = UpiOptionAdapter(options) { position ->
            handleOptionClick(position)
        }

        binding.upiOptionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@UpiActivity)
            adapter = this@UpiActivity.adapter

            // Apply layout animation
            val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(
                this@UpiActivity,
                R.anim.layout_animation
            )
            layoutAnimation = animation
            scheduleLayoutAnimation()
        }
    }

    private fun getUpiOptions(): List<UpiOption> {
        return listOf(
            UpiOption("Send Money to Contact or UPI ID", R.drawable.ic_send),
            UpiOption("Send Money to Account number", R.drawable.ic_file_text),
            UpiOption("Scan Any QR", R.drawable.ic_qr_code),
            UpiOption("Receive Money", R.drawable.ic_receive),
                UpiOption("UPI Mandate", R.drawable.ic_file_text),
            UpiOption("Pending Money Requests", R.drawable.ic_clock),
            UpiOption("Manage UPI ID/Number", R.drawable.ic_settings),
                UpiOption("UPI Transaction History", R.drawable.ic_clock)
        )
    }

    private fun handleOptionClick(position: Int) {
        // Handle option click based on position
        when (position) {
            0 -> {
                // Handle Send Money to Contact
            }

            1 -> {
                // Handle Send Money to Account
            }

            2 -> {
                // Handle Scan QR
            }
            // Handle other options
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}