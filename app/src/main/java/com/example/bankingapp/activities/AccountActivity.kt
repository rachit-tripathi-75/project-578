package com.example.bankingapp.activities

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.adapters.TransactionAdapter
import com.example.bankingapp.classes.Transaction
import com.example.bankingapp.databinding.ActivityAccountBinding


class AccountActivity : AppCompatActivity() {

    private lateinit var adapter: TransactionAdapter
    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        initialisers()
        listeners()
        applyAnimations();

    }

    private fun initialisers() {
        // Set up RecyclerView
        binding.transactionsRecyclerView.setLayoutManager(LinearLayoutManager(this));
        adapter = TransactionAdapter (getTransactionData());
        binding.transactionsRecyclerView.setLayoutManager(LinearLayoutManager (this));
        binding.transactionsRecyclerView.setAdapter(adapter);
    }

    private fun listeners() {

    }

    private fun applyAnimations() {
        // Animate toolbar
        binding.toolbar!!.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right))

        // Animate account card with delay
        Handler().postDelayed({
            binding.accountCard!!.visibility = View.VISIBLE
            binding.accountCard!!.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        }, 200)

        // Animate balance section with delay
        Handler().postDelayed({
            binding.balanceSection!!.visibility = View.VISIBLE
            binding.balanceSection!!.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))
        }, 400)

        // Animate transactions with delay
        Handler().postDelayed({
            binding.transactionsRecyclerView!!.visibility = View.VISIBLE
            binding.transactionsRecyclerView!!.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.slide_in_up
                )
            )
            adapter!!.notifyDataSetChanged()
        }, 600)
    }

    private fun getTransactionData(): List<Transaction> {
        val transactions: MutableList<Transaction> = ArrayList<Transaction>()

        transactions.add(
            Transaction(
                "09", "Oct'23", "UPI/Ravi/58913841/Payment from ph",
                "Chg/Ref No: 4893185985689", 123.50, true
            )
        )

        transactions.add(
            Transaction(
                "09", "Oct'23", "UPI/Amazon Bill Pay/4645149646/Request from Am",
                "Chg/Ref No: 4893185985689", 129.9, true
            )
        )

        transactions.add(
            Transaction(
                "09", "Oct'23", "UPI/Ishwar Dayal/589144/Payment from Ph",
                "Chg/Ref No: 4893185985689", 118.31, false
            )
        )

        transactions.add(
            Transaction(
                "09", "Oct'23", "UPI/Sunil/19495945/Payment from Ph",
                "Chg/Ref No: 4893185985689", 135.0, true
            )
        )

        transactions.add(
            Transaction(
                "09", "Oct'23", "UPI/Amazon Bill Pay/879638663/Request from Am",
                "Chg/Ref No: 4893185985689", 100.0, false
            )
        )

        return transactions
    }
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }



}