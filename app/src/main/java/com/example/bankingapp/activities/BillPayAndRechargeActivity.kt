package com.example.bankingapp.activities

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.adapters.BillCategoryAdapter
import com.example.bankingapp.classes.BillCategory
import com.example.bankingapp.databinding.ActivityBillPayAndRechargeBinding

class BillPayAndRechargeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBillPayAndRechargeBinding
    private lateinit var adapter: BillCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBillPayAndRechargeBinding.inflate(layoutInflater)
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
        val categories = getBillCategories()
        adapter = BillCategoryAdapter(categories) { position ->
            handleCategoryClick(position)
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(this@BillPayAndRechargeActivity, 2)
            adapter = this@BillPayAndRechargeActivity.adapter

            // Apply layout animation
            val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(
                this@BillPayAndRechargeActivity,
                R.anim.layout_animation
            )
            layoutAnimation = animation
            scheduleLayoutAnimation()
        }
    }

    private fun getBillCategories(): List<BillCategory> {
        return listOf(
            BillCategory("Mobile Prepaid", R.drawable.ic_smartphone),
            BillCategory("DTH / Cable", R.drawable.ic_tv),
            BillCategory("Electricity", R.drawable.ic_zap),
            BillCategory("Mobile Postpaid", R.drawable.ic_smartphone),
            BillCategory("Credit Card", R.drawable.ic_credit_card),
            BillCategory("FASTag", R.drawable.ic_user),
            BillCategory("Hospital", R.drawable.ic_building),
            BillCategory("Landline", R.drawable.ic_telephone),
            BillCategory("Insurance", R.drawable.ic_shield),
            BillCategory("Broadband", R.drawable.ic_wifi),
            BillCategory("Mutual Funds", R.drawable.ic_mutual_fund),
            BillCategory("Education", R.drawable.ic_education),
            BillCategory("Donations", R.drawable.ic_donate),
            BillCategory("Water", R.drawable.ic_water)
        )
    }

    private fun handleCategoryClick(position: Int) {
        // Handle category click based on position
        // For example: startActivity(Intent(this, CategoryDetailActivity::class.java))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}