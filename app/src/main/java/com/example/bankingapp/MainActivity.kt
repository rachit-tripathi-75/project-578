package com.example.bankingapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.activities.AccountActivity
import com.example.bankingapp.activities.AccountOverviewActivity
import com.example.bankingapp.activities.BillPayAndRechargeActivity
import com.example.bankingapp.activities.GoldLoanActivity
import com.example.bankingapp.activities.HomeLoanActivity
import com.example.bankingapp.activities.MyPassbookActivity
import com.example.bankingapp.activities.OneTimeTransferActivity
import com.example.bankingapp.activities.PayOverdueEmiActivity
import com.example.bankingapp.activities.PayYourContactActivity
import com.example.bankingapp.activities.PersonalLoanActivity
import com.example.bankingapp.activities.ProfileScreenActivity
import com.example.bankingapp.activities.ScanQrActivity
import com.example.bankingapp.activities.StatementActivity
import com.example.bankingapp.activities.TransferMoneyActivity
import com.example.bankingapp.activities.TwoWheelerLoanActivity
import com.example.bankingapp.activities.UpiActivity
import com.example.bankingapp.adapters.AccountInformationViewPagerAdapter
import com.example.bankingapp.databinding.ActivityMainBinding

// home screen for User (Customer) App...............

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var options: ActivityOptionsCompat
    private var currentPage = 0
    private val DELAY_MS: Long = 1000 // Delay in milliseconds before task is to be executed
    private val PERIOD_MS: Long = 1000 // Time interval between consecutive task executions
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val previousScrollY = 0
    private var isScanQRVisible = true

// implement the Account Activity page, inside the Account Overview page...... as My Passbook has introduced.....

    private val bankingServices = listOf(
        BankingService("Account Overview", R.drawable.account_overview),
//        BankingService("Account Activity", R.drawable.account_activity),
        BankingService("My Passbook", R.drawable.passbook),
        BankingService("Statement", R.drawable.account_statement)
    )

    private val payAndTransferServices = listOf(
        PayAndTransferService("Transfer Money", R.drawable.money_transfer),
        PayAndTransferService("Bill Pay & Recharge", R.drawable.billpay),
        PayAndTransferService("BHIM UPI", R.drawable.upi),
        PayAndTransferService("Scan Any QR", R.drawable.scanqr),
        PayAndTransferService("Pay Your Contact", R.drawable.pay_contact),
        PayAndTransferService("One Time Transfer", R.drawable.one_time_transfer)
    )

    private val loansServices = listOf(
        LoanService("Personal Loan", R.drawable.personal_loan),
        LoanService("Home Loan", R.drawable.home_loan),
        LoanService("Gold Loan", R.drawable.gold_loan),
        LoanService("Pay Day Loan", R.drawable.pay_day_loan),
        LoanService("Pay Overdue EMI", R.drawable.overdue),
        LoanService("Loan and Card Offers", R.drawable.card_offers)
    )

    private val applyNowServices = listOf(
        ApplyNowService("FASTag", R.drawable.fastag),
        ApplyNowService("Investment Account", R.drawable.investment_account),
        ApplyNowService("Apply for Credit Card", R.drawable.ic_dollar),
        ApplyNowService("Pay Day Loan", R.drawable.pay_day_loan),
        ApplyNowService("Personal Loan", R.drawable.personal_loan),
        ApplyNowService("Two Wheeler Loan", R.drawable.bycicle)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        initializeViews()
        listeners()
        createServiceGrids()
        startAnimations()
    }

    private fun initializeViews() {
        binding.viewPager.adapter = AccountInformationViewPagerAdapter(this)
        options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                if (currentPage == (binding.viewPager.adapter?.itemCount ?: 0)) {
                    currentPage = 0
                }
                binding.viewPager.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, PERIOD_MS)
            }
        }
//        binding.nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            if (scrollY > oldScrollY && scrollY > 0 && isScanQRVisible) {
//                // Scrolling down, and it's currently visible
//                slideDown();
//                isScanQRVisible = false;
//            } else if (scrollY < oldScrollY && !isScanQRVisible) {
//                // Scrolling up, and it's currently hidden
//                slideUp();
//                isScanQRVisible = true;
//            }
//        }
    }

    private fun slideDown() {
        val height: Float = binding.llScanQR.height.toFloat()
        val slideDown = TranslateAnimation(0f, 0f, 0f, height)
        slideDown.duration = 300 // Adjust duration as needed
        slideDown.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                binding.llScanQR.setVisibility(View.VISIBLE) // Ensure it's visible before animating
            }

            override fun onAnimationEnd(animation: Animation) {
                binding.llScanQR.setVisibility(View.GONE)
            }

            override fun onAnimationRepeat(animation: Animation) {
                // Not needed
            }
        })
        binding.llScanQR.startAnimation(slideDown)

    }

    private fun slideUp() {
        val height: Float = binding.llScanQR.height.toFloat()
        val slideUp = TranslateAnimation(0f, 0f, height, 0f)
        slideUp.duration = 300 // Adjust duration as needed
        slideUp.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                binding.llScanQR.setVisibility(View.VISIBLE) // Ensure it's visible before animating
            }

            override fun onAnimationEnd(animation: Animation) {
                // No need to change visibility here, it should remain visible
            }

            override fun onAnimationRepeat(animation: Animation) {
                // Not needed
            }
        })
        binding.llScanQR.startAnimation(slideUp)
    }



    private fun listeners() {
        binding.accountActivity.llService.setOnClickListener {
//            startActivity(Intent(this@MainActivity, AccountActivity::class.java))
//            implement the AccountActivity inside the AccountOverview activity......
            startActivity(Intent(this@MainActivity, MyPassbookActivity::class.java), options.toBundle())
        }
        binding.accountOverview.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, AccountOverviewActivity::class.java), options.toBundle())
        }
        binding.bhimUPI.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, UpiActivity::class.java), options.toBundle())
        }
        binding.billPayAndRecharge.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, BillPayAndRechargeActivity::class.java), options.toBundle())
        }
        binding.transferMoney.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, TransferMoneyActivity::class.java), options.toBundle())
        }
        binding.payYourContact.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, PayYourContactActivity::class.java), options.toBundle())
        }
        binding.oneTimeTransfer.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, OneTimeTransferActivity::class.java), options.toBundle())
        }
        binding.personalLoan.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, PersonalLoanActivity::class.java), options.toBundle())
        }
        binding.personalLoan2.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, PersonalLoanActivity::class.java), options.toBundle())
        }
        binding.homeLoan.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, HomeLoanActivity::class.java), options.toBundle())
        }
        binding.payOverdueEMI.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, PayOverdueEmiActivity::class.java), options.toBundle())
        }
        binding.twoWheelerLoan.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, TwoWheelerLoanActivity::class.java), options.toBundle())
        }
        binding.menuIcon.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileScreenActivity::class.java), options.toBundle())
        }
        binding.scanAnyQr.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, ScanQrActivity::class.java), options.toBundle())
        }
        binding.statement.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, StatementActivity::class.java), options.toBundle())
        }
        binding.goldLoan.llService.setOnClickListener {
            startActivity(Intent(this@MainActivity, GoldLoanActivity::class.java), options.toBundle())
        }
        binding.llScanQR.setOnClickListener {
            startActivity(Intent(this@MainActivity, ScanQrActivity::class.java), options.toBundle())
        }
    }


    private fun createServiceGrids() {
        createServiceGrid(binding.glBanking, bankingServices)
        createServiceGrid(binding.glPayAndTransfer, payAndTransferServices)
        createServiceGrid(binding.glLoan, loansServices)
        createServiceGrid(binding.glApplyNow, applyNowServices)
    }

    private fun <T : Any> createServiceGrid(grid: GridLayout, services: List<T>) {
        services.forEachIndexed { index, service ->
            val serviceView = layoutInflater.inflate(R.layout.item_service, null)
            val icon = serviceView.findViewById<ImageView>(R.id.serviceIcon)
            val name = serviceView.findViewById<TextView>(R.id.serviceName)

            when (service) {
                is BankingService -> {
                    icon.setImageResource(service.iconRes)
                    name.text = service.name
                }

                is PayAndTransferService -> {
                    icon.setImageResource(service.iconRes)
                    name.text = service.name
                }

                is LoanService -> {
                    icon.setImageResource(service.iconRes)
                    name.text = service.name
                }

                is ApplyNowService -> {
                    icon.setImageResource(service.iconRes)
                    name.text = service.name
                }
            }

            setupGridItem(grid, serviceView, index)
        }
    }

    private fun setupGridItem(grid: GridLayout, view: View, index: Int) {
        view.alpha = 0f
        view.translationY = 50f

        val params = GridLayout.LayoutParams().apply {
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            columnSpec = GridLayout.spec(index % 3, 1f)
            rowSpec = GridLayout.spec(index / 3)
            setMargins(8, 8, 8, 8)
        }

        grid.addView(view, params)

        view.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(300)
            .setStartDelay(100 + (index * 50).toLong())
            .start()
    }

    private fun startAnimations() {

        // Animating account section.....

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.llAccountInformation, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(binding.llAccountInformation, "translationY", -20f, 0f)
            )
            duration = 1200
            startDelay = 200
            interpolator = DecelerateInterpolator()
            start()
        }

        // Animating banking section........

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.llBanking, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(binding.llAccountInformation, "translationY", -20f, 0f)
            )
            duration = 1200
            startDelay = 200
            interpolator = DecelerateInterpolator()
            start()
        }

        // Animating pay and transfer section........

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.llPayAndTransfer, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(binding.llAccountInformation, "translationY", -20f, 0f)
            )
            duration = 1200
            startDelay = 200
            interpolator = DecelerateInterpolator()
            start()
        }

        // Animating loans section........

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.llLoans, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(binding.llAccountInformation, "translationY", -20f, 0f)
            )
            duration = 1200
            startDelay = 200
            interpolator = DecelerateInterpolator()
            start()
        }

        // Animating apply now section........

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.llApplyNow, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(binding.llAccountInformation, "translationY", -20f, 0f)
            )
            duration = 1200
            startDelay = 200
            interpolator = DecelerateInterpolator()
            start()
        }


    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, DELAY_MS)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    data class BankingService(val name: String, val iconRes: Int)
    data class PayAndTransferService(val name: String, val iconRes: Int)
    data class LoanService(val name: String, val iconRes: Int)
    data class ApplyNowService(val name: String, val iconRes: Int)
}





