package com.example.bankingapp.agent.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.R
import com.example.bankingapp.databinding.ActivityStatementAgentBinding

class StatementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatementAgentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStatementAgentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listeners()
    }

    private fun listeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnShowStatement.setOnClickListener {
            startActivity(Intent(this@StatementActivity, AccountStatementActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}