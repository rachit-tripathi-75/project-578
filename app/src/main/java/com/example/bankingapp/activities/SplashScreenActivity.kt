package com.example.bankingapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.LoginActivity
import com.example.bankingapp.MainActivity
import com.example.bankingapp.R
import com.example.bankingapp.agent.HomeActivity
import com.example.bankingapp.classes.PrefsManager
import com.example.bankingapp.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()

    }

    private fun initialisers() {

        Handler(Looper.getMainLooper()).postDelayed({
            if (PrefsManager.getSession(this)) {
                when (PrefsManager.getUserType(this)) {
                    0 -> {
                        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                        finish()
                    }
                    1 -> {
                        startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                        finish()
                    }
                    else -> {
                        Toast.makeText(this@SplashScreenActivity, "An error occurred while configuring the user. Try again later", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
            }
        }, 1500)


    }


}