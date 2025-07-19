package com.example.bankingapp.agent.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.R
import com.example.bankingapp.databinding.ActivityAccountStatementBinding

class AccountStatementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountStatementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountStatementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listeners()

    }

    private fun listeners() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }


}