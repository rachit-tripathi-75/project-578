package com.example.bankingapp.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.LoginActivity
import com.example.bankingapp.R
import com.example.bankingapp.classes.PrefsManager
import com.example.bankingapp.databinding.ActivityProfileScreenBinding

class ProfileScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window = window
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        window.statusBarColor = Color.BLACK
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()
        listeners()

    }

    private fun initialisers() {

    }

    private fun listeners() {
        binding.logoutOption.setOnClickListener {
            // Handling logout....... and deleting all the data stored in the shared preference.......
            startActivity(Intent(this@ProfileScreenActivity, LoginActivity::class.java))
            finishAffinity()
            getSharedPreferences(PrefsManager.PREF_NAME, MODE_PRIVATE).edit() {
                clear()
            }
            getSharedPreferences(PrefsManager.PIN_SHOW_PREF_NAME, MODE_PRIVATE).edit() {
                clear()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}