package com.example.bankingapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.activities.SplashScreenActivity
import com.example.bankingapp.classes.ApiClient
import com.example.bankingapp.classes.PrefsManager
import com.example.bankingapp.databinding.ActivityLoginBinding
import com.example.bankingapp.responses.LoginResponse
import retrofit2.Call


class LoginActivity : AppCompatActivity() {

    private val userTypes = arrayOf("User", "Agent")
    private var userTypeInt = 0
    private lateinit var binding: ActivityLoginBinding
    private var userType: String = "user"
    private var showPassword = false
    private lateinit var pinDigits: List<EditText>

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
        // Initial setup - show selection screen
        showSelectionScreen()

        // Set up click listeners
        setupClickListeners()

        // Set up PIN input behavior
        setupPinInputs()

        listeners()

    }


    private fun setupClickListeners() {
        // Continue button on selection screen
        binding.continueButton.setOnClickListener {
            when {
                binding.userRadioButton.isChecked -> {
                    userType = "user"
                    userTypeInt = 0
                    showCredentialsScreen()
                    showPinScreen()
                }

                binding.agentRadioButton.isChecked -> {
                    userType = "agent"
                    userTypeInt = 1
                    showCredentialsScreen()
                    showPinScreen()
                }

                else -> Toast.makeText(this, "Please select an account type", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Back button
        binding.backButton.setOnClickListener {
            showSelectionScreen()
        }

        // Toggle password visibility
        binding.togglePasswordVisibility.setOnClickListener {
            togglePasswordVisibility()
        }

        // Login button
        binding.loginButton.setOnClickListener {
            // Handle login logic here
            Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show()
        }

        // Toggle between PIN and credentials
        binding.togglePinMode.setOnClickListener {
            showCredentialsScreen()
            showPinScreen()
//            if (binding.pinContainer.visibility == View.VISIBLE) {
//                showCredentialsScreen()
//            } else {
//                showPinScreen()
//            }
        }
    }

    private fun showSelectionScreen() {
        // Hide back button
        binding.backButton.visibility = View.GONE
        binding.llLoginBtnSignInWithPIN.visibility = View.GONE

        // Show selection container, hide others
        binding.selectionContainer.visibility = View.VISIBLE
        binding.credentialsContainer.visibility = View.GONE
        binding.pinContainer.visibility = View.GONE
        binding.llDivder.visibility = View.GONE

        // Apply animation
        binding.selectionContainer.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.fade_in
            )
        )

        // Reset radio buttons
        binding.userTypeRadioGroup.clearCheck()
    }

    private fun showCredentialsScreen() {
        // Update UI based on user type
//        binding.llLoginBtnSignInWithPIN.visibility = View.VISIBLE
        binding.userIdLabel.text = if (userType == "user") "User ID" else "Agent ID"
        binding.etId.hint = if (userType == "user") "Enter User ID" else "Enter Agent ID"

        // Show back button
        binding.backButton.visibility = View.VISIBLE

        // Show credentials container, hide others
        binding.selectionContainer.visibility = View.GONE
        binding.credentialsContainer.visibility = View.VISIBLE
        binding.llDivder.visibility = View.VISIBLE
//        binding.pinContainer.visibility = View.GONE

        // Apply animation
        binding.credentialsContainer.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.slide_in_right
            )
        )

        // Update button text
        binding.togglePinMode.text = "Sign in with PIN"
        binding.loginButton.text = "Login"
    }

    private fun showPinScreen() {
        // Show back button
        binding.backButton.visibility = View.VISIBLE

        // Show PIN container, hide others
        binding.selectionContainer.visibility = View.GONE
//        binding.credentialsContainer.visibility = View.GONE
        binding.pinContainer.visibility = View.VISIBLE
        binding.llDivder.visibility = View.VISIBLE

        // Apply animation
        binding.pinContainer.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.slide_in_right
            )
        )

        // Update title and button text
        binding.pinTitle.text =
            "Enter 4-digit mPIN for ${if (userType == "user") "User" else "Agent"}"
        binding.togglePinMode.text = "Sign in with Credentials"
        binding.loginButton.text = "Verify PIN"

        // Clear and focus first PIN input
        clearPinInputs()
        binding.pin1.requestFocus()
    }

    private fun togglePasswordVisibility() {
        showPassword = !showPassword
        if (showPassword) {
            binding.etPassword.transformationMethod = null
            binding.togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off)
        } else {
            binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.togglePasswordVisibility.setImageResource(R.drawable.ic_visibility)
        }
        // Maintain cursor position
        binding.etPassword.setSelection(binding.etPassword.text.length)
    }

    private fun setupPinInputs() {
        // Set up text changed listeners for auto-focus
//        binding.pin1.addTextChangedListener { if (it?.length == 1) binding.pin2.requestFocus() }
//        binding.pin2.addTextChangedListener { if (it?.length == 1) binding.pin3.requestFocus() }
//        binding.pin3.addTextChangedListener { if (it?.length == 1) binding.pin4.requestFocus() }
//        binding.pin4.addTextChangedListener { if (it?.length == 1) binding.pin5.requestFocus() }
//        binding.pin5.addTextChangedListener { if (it?.length == 1) binding.pin6.requestFocus() }


        // Focus on first digit initially
        pinDigits[0].requestFocus()

        // Set up text watchers for each PIN digit
        pinDigits.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        // Mark this digit as filled
                        editText.isActivated = true

                        // Add completion indicator
                        addCompletionIndicator(editText)

                        // Move to next digit if not the last one
                        if (index < pinDigits.size - 1) {
                            pinDigits[index + 1].requestFocus()
                        } else {
                            // Hide keyboard if all digits are filled
                            editText.clearFocus()
                        }
                    } else {
                        // Remove completion indicator if digit is cleared
                        editText.isActivated = false
                        removeCompletionIndicator(editText)
                    }

                    // Update continue button state
                    updateContinueButtonState()
                }
            })

            // Handle backspace key to move to previous digit
            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL && event.action == android.view.KeyEvent.ACTION_DOWN && editText.text.isEmpty() && index > 0) {
                    pinDigits[index - 1].requestFocus()
                    pinDigits[index - 1].text.clear()
                    true
                } else {
                    false
                }
            }
        }


    }



    private fun addCompletionIndicator(editText: EditText) {
        // In a real app, you would add a small indicator view next to the EditText
        // For simplicity, we're just changing the background here
        editText.setBackgroundResource(R.drawable.pin_edit_text_background)

        // Add a small scale animation
        val scaleX = ObjectAnimator.ofFloat(editText, "scaleX", 1f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(editText, "scaleY", 1f, 1.1f, 1f)
        val animSet = AnimatorSet()
        animSet.playTogether(scaleX, scaleY)
        animSet.duration = 300
        animSet.start()
    }

    private fun removeCompletionIndicator(editText: EditText) {
        // Reset the background
        editText.setBackgroundResource(R.drawable.pin_edit_text_background)
    }

    private fun updateContinueButtonState() {
        // Enable continue button only if all digits are filled
        binding.btnLoginWithMPin.isEnabled = pinDigits.all { it.text.isNotEmpty() }

        if (binding.btnLoginWithMPin.isEnabled) {
            // Add a subtle animation to the button when it becomes enabled
            val animation = AnimationUtils.loadAnimation(this, R.anim.pulse)
            binding.btnLoginWithMPin.startAnimation(animation)
        }
    }


    private fun clearPinInputs() {
        binding.pin1.setText("")
        binding.pin2.setText("")
        binding.pin3.setText("")
        binding.pin4.setText("")
    }


    private fun initialisers() {
//        setUpTabs()
        pinDigits = listOf(
            findViewById(R.id.pin1),
            findViewById(R.id.pin2),
            findViewById(R.id.pin3),
            findViewById(R.id.pin4),
        )
    }

    private fun listeners() {
        binding.btnLoginWithCredentials.setOnClickListener {
            if (isValidSignIn()) {
                signInWithCredentials()
            }
        }

        binding.btnLoginWithMPin.setOnClickListener {
            signInWithMPin()
        }



    }



    private fun isValidSignIn(): Boolean {
        if (binding.etId.text.toString().isEmpty()) {
            binding.etId.setError("Enter your user ID")
            return false
        } else if (binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.setError("Enter your password")
            return false
        }
        return true
    }

    private fun signInWithCredentials() {
        binding.btnLoginWithCredentials.visibility = View.GONE
//        binding.progressBar.visibility = View.VISIBLE

        ApiClient.loginInstance.login(
            "ci_session=4ko0di4js59huoo8vlkmqst62813i2jv",
            binding.etId.text.toString(),
            binding.etPassword.text.toString()).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                binding.btnLoginWithCredentials.visibility = View.VISIBLE
//                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d("loginResponseTAG", response.body()?.msg  .toString())
                    val loginData = response.body()
                    if (loginData?.status == 1) {
                        PrefsManager.setUserInformation(applicationContext, loginData)
                        PrefsManager.setSession(applicationContext, true)
                        Log.d("userType", "user: $userType")
                        PrefsManager.setUserType(applicationContext, userTypeInt) //  0 --> for user (customer)........
                                                                                    // 1 --> for agent...........
                        startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
                        finish()
                    }

                } else {
                    Log.d("loginResponseTAG", response.body()?.msg.toString())
                    Toast.makeText(applicationContext, "Incorrect Credentials", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.btnLoginWithCredentials.visibility = View.VISIBLE
//                binding.progressBar.visibility = View.GONE
                Log.d("loginResponseTAG", "Error: ${t.message}")
                Toast.makeText(applicationContext, "An error has occurred. Please try again later", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun signInWithMPin() {

        binding.btnLoginWithMPin.visibility = View.GONE
//        binding.progressBar.visibility = View.VISIBLE

        val pin = pinDigits.joinToString("") { it.text.toString() }

        ApiClient.loginWithMPinInstance.loginWithMPin(
            "Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
            "ci_session=k95qcmfp6di79vbid99u903plt7v5u10",
            pin,
            getAndroidId(this)).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                binding.btnLoginWithMPin.visibility = View.VISIBLE
//                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d("loginResponseTAG", response.body()?.msg  .toString())
                    val loginData = response.body()
                    if (loginData?.status == 1) {
                        PrefsManager.setUserInformation(applicationContext, loginData)
                        PrefsManager.setSession(applicationContext, true)
                        PrefsManager.setShouldShowCreatePinBottomSheetDialog(applicationContext, false)
                        Log.d("userType", "user: $userType")
                        PrefsManager.setUserType(applicationContext, userTypeInt) //  0 --> for user (customer)........
                                                                                    // 1 --> for agent...........
                        startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
                        finish()
                    }

                } else {
                    Log.d("loginResponseTAG", response.body()?.msg.toString())
                    clearPinInputs()
                    Toast.makeText(applicationContext, "Incorrect Credentials", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.btnLoginWithMPin.visibility = View.VISIBLE
//                binding.progressBar.visibility = View.GONE
                Log.d("loginResponseTAG", "Error: ${t.message}")
                clearPinInputs()
                Toast.makeText(applicationContext, "An error has occurred. Please try again later", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getAndroidId(context: Context): String {
        Log.d("androidIdTAG", Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
        val pin = pinDigits.joinToString("") { it.text.toString() }
        Log.d("pinTAG", pin)
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
















//
//    private fun setUpTabs() {
//
//        // Set up the ViewPager with the sections adapter
//        val pagerAdapter = LoginPagerAdapter(this)
//        binding.viewPager.adapter = pagerAdapter
//        binding.viewPager.isUserInputEnabled = true
//
//        // Connect the TabLayout with the ViewPager
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            tab.text = userTypes[position]
//        }.attach()
//
//    }
//
//    inner class LoginPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
//        override fun getItemCount(): Int = userTypes.size
//
//        override fun createFragment(position: Int): Fragment {
//            // Return a new instance of the appropriate fragment
//            return when (position) {
//                0 -> UserLoginContainerFragment()
//                1 -> AgentLoginContainerFragment()
//                else -> throw IllegalArgumentException("Invalid position: $position")
//            }
//        }
//    }


}