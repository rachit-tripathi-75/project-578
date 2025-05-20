package com.example.bankingapp.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.bankingapp.MainActivity
import com.example.bankingapp.R
import com.example.bankingapp.agent.HomeActivity
import com.example.bankingapp.databinding.ActivityEnterMpinBinding
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EnterMPinActivity : AppCompatActivity() {

    private lateinit var pinDigits: List<EditText>
    private lateinit var errorText: TextView
    private lateinit var continueButton: MaterialButton
    private lateinit var lockIcon: ImageView
    private lateinit var iconContainer: CardView
    private lateinit var cardView: CardView
    private lateinit var successLayout: ConstraintLayout
    private lateinit var binding: ActivityEnterMpinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEnterMpinBinding.inflate(layoutInflater)
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
        // Initialize views
        pinDigits = listOf(
            findViewById(R.id.pinDigit1),
            findViewById(R.id.pinDigit2),
            findViewById(R.id.pinDigit3),
            findViewById(R.id.pinDigit4),
            findViewById(R.id.pinDigit5),
            findViewById(R.id.pinDigit6)
        )
        errorText = findViewById(R.id.errorText)
        continueButton = findViewById(R.id.continueButton)
        lockIcon = findViewById(R.id.lockIcon)
        iconContainer = findViewById(R.id.iconContainer)
        cardView = findViewById(R.id.cardView)

        // Success layout is initially hidden and will be shown on successful PIN entry
//        successLayout = findViewById(R.id.successLayout)
//        successLayout.visibility = View.GONE

        // Set up PIN input fields
        setupPinInputs()

        // Set up continue button
        setupContinueButton()

        // Animate the lock icon
        animateLockIcon()
    }

    private fun listeners() {

    }

    private fun setupPinInputs() {
        // Focus on first digit initially
        pinDigits[0].requestFocus()

        // Set up text watchers for each PIN digit
        pinDigits.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

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
        continueButton.isEnabled = pinDigits.all { it.text.isNotEmpty() }

        if (continueButton.isEnabled) {
            // Add a subtle animation to the button when it becomes enabled
            val animation = AnimationUtils.loadAnimation(this, R.anim.pulse)
            continueButton.startAnimation(animation)
        }
    }

    private fun setupContinueButton() {
        continueButton.isEnabled = false

        continueButton.setOnClickListener {
            val pin = pinDigits.joinToString("") { it.text.toString() }

            if (pin.length < 4) {
                showError("Please enter all 4 digits")
                return@setOnClickListener
            }

            // Show loading state
            continueButton.isEnabled = false
            continueButton.text = "Verifying..."
            continueButton.icon = null

            // Add a circular progress indicator
            val progressDrawable = ContextCompat.getDrawable(this, R.drawable.circular_progress)
            continueButton.icon = progressDrawable

            // Verify PIN (simulated)
            verifyPin(pin)
        }
    }

    private fun verifyPin(pin: String) {
        // In a real app, you would verify the PIN against a stored value
        // For this demo, we'll simulate a verification delay and success

        CoroutineScope(Dispatchers.Main).launch {
            // Simulate network delay
            delay(1500)

            // For demo purposes, any 6-digit PIN is considered valid
            // In a real app, you would validate against a stored PIN
            showSuccess()
        }
    }

    private fun showError(message: String) {
        errorText.text = message
        errorText.visibility = View.VISIBLE

        // Shake animation for error
        val animation = AnimationUtils.loadAnimation(this, R.anim.shake)
        errorText.startAnimation(animation)

        // Also shake the PIN container
        val pinContainer = findViewById<View>(R.id.pinContainer)
        pinContainer.startAnimation(animation)
    }

    private fun showSuccess() {
        // Animate the lock icon to unlock icon
        val unlockAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        lockIcon.startAnimation(unlockAnimation)

        unlockAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Change lock icon to unlock
                iconContainer.setCardBackgroundColor(ContextCompat.getColor(this@EnterMPinActivity, R.color.green_100))

                // Animate card to success state
                animateToSuccessState()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }



    private fun animateToSuccessState() {
        // Fade out the PIN input layout
        val fadeOut = ObjectAnimator.ofFloat(findViewById(R.id.pinContainer), "alpha", 1f, 0f)
        fadeOut.duration = 300

        // Fade out the error text if visible
        if (errorText.isVisible) {
            ObjectAnimator.ofFloat(errorText, "alpha", 1f, 0f).start()
        }

        // Fade out the continue button
        val buttonFadeOut = ObjectAnimator.ofFloat(continueButton, "alpha", 1f, 0f)
        buttonFadeOut.duration = 300

        // Fade out the forgot PIN text
        val forgotPinFadeOut = ObjectAnimator.ofFloat(findViewById(R.id.forgotPinText), "alpha", 1f, 0f)
        forgotPinFadeOut.duration = 300

        // Play all fade out animations together
        val fadeOutSet = AnimatorSet()
        fadeOutSet.playTogether(fadeOut, buttonFadeOut, forgotPinFadeOut)
        fadeOutSet.start()

        fadeOutSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                // Show success layout
//                successLayout.visibility = View.VISIBLE
//                progressBar.visibility = View.VISIBLE

                // Update title and subtitle
                findViewById<TextView>(R.id.titleText).text = "Success!"
                findViewById<TextView>(R.id.subtitleText).text = "Redirecting to home screen..."

                // Fade in success elements
//                val fadeIn = ObjectAnimator.ofFloat(successLayout, "alpha", 0f, 1f)
//                fadeIn.duration = 300
//                fadeIn.start()

                // Animate progress bar
//                val progressAnim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
//                progressAnim.duration = 1500
//                progressAnim.interpolator = AccelerateDecelerateInterpolator()
//                progressAnim.start()

                // Navigate to home screen after delay
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1500)
                    navigateToHomeScreen()
                }
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {}

            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })
    }

    private fun navigateToHomeScreen() {
        // In a real app, you would navigate to your home screen activity
        startActivity(Intent(this, MainActivity::class.java))

        // Add a nice transition animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        // Finish this activity so user can't go back to PIN screen with back button
        finish()
    }

    private fun animateLockIcon() {
        // Add a subtle pulse animation to the lock icon
        val scaleX = ObjectAnimator.ofFloat(iconContainer, "scaleX", 1f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(iconContainer, "scaleY", 1f, 1.1f, 1f)
        val rotate = ObjectAnimator.ofFloat(lockIcon, "rotation", 0f, 5f, -5f, 0f)

        val animSet = AnimatorSet()
        animSet.playTogether(scaleX, scaleY, rotate)
        animSet.duration = 1000
        animSet.startDelay = 500
        animSet.start()
    }



}