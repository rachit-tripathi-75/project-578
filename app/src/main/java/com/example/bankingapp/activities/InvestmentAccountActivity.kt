package com.example.bankingapp.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.adapters.HoldingSharesAdapter
import com.example.bankingapp.databinding.ActivityInvestmentAccountBinding
import com.example.bankingapp.models.HoldingShares
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class InvestmentAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInvestmentAccountBinding
    private lateinit var holdingsAdapter: HoldingSharesAdapter
    private var showBalance = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInvestmentAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        setupRecyclerView()
        loadData()

    }



    private fun setupUI() {
        binding.btnToggleBalance.setOnClickListener {
            showBalance = !showBalance
            updateBalanceVisibility()
        }

        binding.btnBuy.setOnClickListener {
            // Handle buy action
        }

        binding.btnSell.setOnClickListener {
            // Handle sell action
        }

        binding.btnAnalysis.setOnClickListener {
            // Handle analysis action
        }

        binding.btnReports.setOnClickListener {
            // Handle reports action
        }

        binding.cardAddFunds.setOnClickListener {
            // Handle add funds action
        }
    }

    private fun setupRecyclerView() {
        holdingsAdapter = HoldingSharesAdapter()
        binding.recyclerViewHoldings.apply {
            layoutManager = LinearLayoutManager(this@InvestmentAccountActivity)
            adapter = holdingsAdapter
        }
    }

    private fun loadData() {
        val holdings = listOf(
            HoldingShares("RELIANCE", "Reliance Industries Ltd", 25, 2456.75, 58500.0, 61418.75, "R"),
            HoldingShares("TCS", "Tata Consultancy Services", 15, 3842.30, 54000.0, 57634.50, "T"),
            HoldingShares("INFY", "Infosys Limited", 40, 1456.80, 56000.0, 58272.00, "I"),
            HoldingShares("HDFCBANK", "HDFC Bank Limited", 35, 1678.45, 57500.0, 58745.75, "H"),
            HoldingShares("ICICIBANK", "ICICI Bank Limited", 50, 1234.60, 59000.0, 61730.00, "I")
        )

        holdingsAdapter.submitList(holdings)
        updatePortfolioSummary(holdings)
        updateBalanceVisibility()
    }

    private fun updatePortfolioSummary(holdings: List<HoldingShares>) {
        val totalInvested = holdings.sumOf { it.investedValue }
        val totalCurrent = holdings.sumOf { it.currentValue }
        val totalGain = totalCurrent - totalInvested
        val totalGainPercent = (totalGain / totalInvested) * 100

        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        formatter.currency = Currency.getInstance("INR")

        binding.tvTotalValue.text = formatter.format(totalCurrent)
        binding.tvInvestedAmount.text = "Invested: ${formatter.format(totalInvested)}"

        if (totalGain >= 0) {
            binding.tvGainLoss.text = "+${formatter.format(totalGain)} (+${String.format("%.2f", totalGainPercent)}%)"
            binding.tvGainLoss.setTextColor(getColor(R.color.green_600))
            binding.ivGainLossIcon.setImageResource(R.drawable.ic_trending_up)
        } else {
            binding.tvGainLoss.text = "${formatter.format(totalGain)} (${String.format("%.2f", totalGainPercent)}%)"
            binding.tvGainLoss.setTextColor(getColor(R.color.red_600))
            binding.ivGainLossIcon.setImageResource(R.drawable.ic_trending_down)
        }

        binding.tvHoldingsCount.text = "${holdings.size} stocks"
    }

    private fun updateBalanceVisibility() {
        if (showBalance) {
            binding.tvTotalValue.visibility = View.VISIBLE
            binding.tvHiddenBalance.visibility = View.GONE
            binding.btnToggleBalance.setImageResource(R.drawable.ic_eye)
        } else {
            binding.tvTotalValue.visibility = View.GONE
            binding.tvHiddenBalance.visibility = View.VISIBLE
            binding.btnToggleBalance.setImageResource(R.drawable.ic_eye_off)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}