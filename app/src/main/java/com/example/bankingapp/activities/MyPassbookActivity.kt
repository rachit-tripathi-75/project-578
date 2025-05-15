package com.example.bankingapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.adapters.TransactionItem
import com.example.bankingapp.adapters.TransactionPassbookAdapter
import com.example.bankingapp.databinding.ActivityMyPassbookBinding

class MyPassbookActivity : AppCompatActivity() {


    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var adapter: TransactionPassbookAdapter
    private lateinit var binding: ActivityMyPassbookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyPassbookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()
        listeners()

    }


    private fun initialisers() {
        setupRecyclerView()
    }

    private fun listeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }


    private fun setupRecyclerView() {
        adapter = TransactionPassbookAdapter(getTransactionData())
        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.transactionsRecyclerView.adapter = adapter
    }

    private fun getTransactionData(): List<TransactionItem> {
        val transactions = mutableListOf<TransactionItem>()

//         Add opening balance
        transactions.add(TransactionItem.OpeningBalance("107.00"))

        // Add transactions
        transactions.add(
            TransactionItem.Transaction(
                "09-02-2025",
                "12.00 Dr",
                "95.00 Cr",
                "IMPS-50371631952-Vishal Singh-ICIC0006288-*******1915-Saving Account"
            )
        )

        transactions.add(
            TransactionItem.Transaction(
                "09-02-2025",
                "12.00 Cr",
                "107.00 Cr",
                "IMPS-50371631952-Vishal Singh-ICIC0006288-*******1915-Saving Account"
            )
        )

        transactions.add(
            TransactionItem.Transaction(
                "09-02-2025",
                "12.00 Dr",
                "95.00 Cr",
                "IMPS-50371631360-Vishal Singh-ICIC0006288-*******1915-Saving Account"
            )
        )

        transactions.add(
            TransactionItem.Transaction(
                "09-02-2025",
                "12.00 Cr",
                "107.00 Cr",
                "IMPS-50371631360-Vishal Singh-ICIC0006288-*******1915-Saving Account"
            )
        )

        transactions.add(
            TransactionItem.Transaction(
                "07-02-2025",
                "15.00 Dr",
                "92.00 Cr",
                "RETURN IMPS-50371631360-Vishal Singh-ICIC0006288-*******1915"
            )
        )

        transactions.add(
            TransactionItem.Transaction(
                "13-03-2025",
                "10.00 Cr",
                "102.00 Cr",
                "GL 219 IMPS-50721449126-JFFAT FATIMA-JKDK0000123-*******573"
            )
        )

        transactions.add(
            TransactionItem.Transaction(
                "18-03-2025",
                "10.00 Cr",
                "112.00 Cr",
                "GL 219 IMPS-50761636478-JFFAT FATIMA-JKDK0000123-*******573"
            )
        )

        transactions.add(
            TransactionItem.Transaction(
                "18-03-2025",
                "10.00 Cr",
                "122.00 Cr",
                "GL 219 IMPS-50771276410-JFFAT FATIMA-JKDK0000123-*******573"
            )
        )

        transactions.add(
            TransactionItem.Transaction(
                "30-03-2025",
                "1.00 Cr",
                "123.00 Cr",
                "Int From 01-01-2025 To 31-03-2025"
            )
        )

        transactions.add(
            TransactionItem.Transaction(
                "21-04-2025",
                "1.00 Cr",
                "124.00 Cr",
                "UPI ID: 54777328975-VISHAL SINGH 3768722163"
            )
        )

        return transactions
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}