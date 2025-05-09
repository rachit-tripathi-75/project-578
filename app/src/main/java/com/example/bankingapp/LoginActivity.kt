package com.example.bankingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.adapters.LoginPageAdapter
import com.example.bankingapp.databinding.ActivityLoginBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()

    }

    private fun initialisers() {
        setUpTabs()
    }

    private fun setUpTabs() {

        // Set up ViewPager with adapter
        binding.viewPager.adapter = LoginPageAdapter(this)


        // Connect TabLayout with ViewPager
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.setText(if (position == 0) "User" else "Agent")
        }.attach()


        // Customize tab appearance
        for (i in 0..<binding.tabLayout.tabCount) {
            val tab = binding.tabLayout.getTabAt(i)
            tab?.view?.setPadding(32, 16, 32, 16)
        }
    }


}