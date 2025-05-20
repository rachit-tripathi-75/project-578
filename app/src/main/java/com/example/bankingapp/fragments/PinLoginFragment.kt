package com.example.bankingapp.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.bankingapp.MainActivity
import com.example.bankingapp.R
import com.example.bankingapp.databinding.FragmentPinLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PinLoginFragment : Fragment() {

    private lateinit var binding: FragmentPinLoginBinding
    private lateinit var pinDigits: List<EditText>

    private lateinit var userType: String
    private var switchLoginMethodListener: (() -> Unit)? = null

    companion object {
        private const val ARG_USER_TYPE = "user_type"

        fun newInstance(userType: String): PinLoginFragment {
            val fragment = PinLoginFragment()
            val args = Bundle()
            args.putString(ARG_USER_TYPE, userType)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userType = it.getString(ARG_USER_TYPE, "user")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinLoginBinding.inflate(inflater, container, false)

        initialisers()
        listeners()
        return binding.root
    }

    private fun initialisers() {
        // Initialize views
        pinDigits = listOf(
            binding.pinDigit1,
            binding.pinDigit2,
            binding.pinDigit3,
            binding.pinDigit4,
            binding.pinDigit5,
            binding.pinDigit6
        )

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
        binding.continueButton.isEnabled = pinDigits.all { it.text.isNotEmpty() }

        if (binding.continueButton.isEnabled) {
            // Add a subtle animation to the button when it becomes enabled
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse)
            binding.continueButton.startAnimation(animation)
        }
    }

    private fun setupContinueButton() {
        binding.continueButton.isEnabled = false

        binding.continueButton.setOnClickListener {
            val pin = pinDigits.joinToString("") { it.text.toString() }

            if (pin.length < 6) {
                showError("Please enter all 6 digits")
                return@setOnClickListener
            }

            // Show loading state
            binding.continueButton.isEnabled = false
            binding.continueButton.text = "Verifying..."
            binding.continueButton.icon = null

            // Add a circular progress indicator
            val progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.circular_progress)
            binding.continueButton.icon = progressDrawable

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
        binding.errorText.text = message
        binding.errorText.visibility = View.VISIBLE

        // Shake animation for error
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        binding.errorText.startAnimation(animation)

        // Also shake the PIN container
        binding.pinContainer.startAnimation(animation)
    }

    private fun showSuccess() {
        // Animate the lock icon to unlock icon
        val unlockAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
        binding.lockIcon.startAnimation(unlockAnimation)

        unlockAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Change lock icon to unlock
                binding.iconContainer.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_100))

                // Animate card to success state
                animateToSuccessState()
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })
    }

    private fun animateToSuccessState() {
        // Fade out the PIN input layout
        val fadeOut = ObjectAnimator.ofFloat(binding.pinContainer, "alpha", 1f, 0f)
        fadeOut.duration = 300

        // Fade out the error text if visible
        if (binding.errorText.isVisible) {
            ObjectAnimator.ofFloat(binding.errorText, "alpha", 1f, 0f).start()
        }

        // Fade out the continue button
        val buttonFadeOut = ObjectAnimator.ofFloat(binding.continueButton, "alpha", 1f, 0f)
        buttonFadeOut.duration = 300

        // Fade out the forgot PIN text
        val forgotPinFadeOut = ObjectAnimator.ofFloat(binding.forgotPinText, "alpha", 1f, 0f)
        forgotPinFadeOut.duration = 300

        // Play all fade out animations together
        val fadeOutSet = AnimatorSet()
        fadeOutSet.playTogether(buttonFadeOut, forgotPinFadeOut)
        fadeOutSet.start()

        fadeOutSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}

            override fun onAnimationEnd(animation: android.animation.Animator) {
                // Show success layout
                //                successLayout.visibility = View.VISIBLE
                //                progressBar.visibility = View.VISIBLE

                // Update title and subtitle
                binding.titleText.text = "Success!"
                binding.subtitleText.text = "Redirecting to home screen..."

                //                 Fade in success elements
                //                val fadeIn = ObjectAnimator.ofFloat(successLayout, "alpha", 0f, 1f)
                //                fadeIn.duration = 300
                //                fadeIn.start()
                //
                //                 Animate progress bar
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
        startActivity(Intent(requireContext(), MainActivity::class.java))

        // Add a nice transition animation
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        // Finish this activity so user can't go back to PIN screen with back button
        activity?.finish()
    }

    private fun animateLockIcon() {
        // Add a subtle pulse animation to the lock icon
        //        val scaleX = ObjectAnimator.ofFloat(iconContainer, "scaleX", 1f, 1.1f, 1f)
        //        val scaleY = ObjectAnimator.ofFloat(iconContainer, "scaleY", 1f, 1.1f, 1f)
        //        val rotate = ObjectAnimator.ofFloat(lockIcon, "rotation", 0f, 5f, -5f, 0f)

        //        val animSet = AnimatorSet()
        //        animSet.playTogether(scaleX, scaleY, rotate)
        //        animSet.duration = 1000
        //        animSet.startDelay = 500
        //        animSet.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update UI based on user type
        if (userType == "agent") {
            //            binding.pinSubtitle.text = "Please enter your 4-digit PIN to sign in as Agent"
            //            binding.switchToCredentials.text = "Sign in with Agent ID and Password"
        } else {
            //            binding.pinSubtitle.text = "Please enter your 4-digit PIN to sign in as User"
            //            binding.switchToCredentials.text = "Sign in with User ID and Password"
        }

        // Set up PIN input auto-focus
        setupPinInputs()

//        binding.continueButton.setOnClickListener {
//            val pin =
//                "${binding.pinDigit1.text}${binding.pinDigit2.text}${binding.pinDigit3.text}${binding.pinDigit4.text}${binding.pinDigit5.text}${binding.pinDigit6.text}"
//
//            if (pin.length == 6) {
//                // Perform PIN login
//                Toast.makeText(
//                    context,
//                    "Logging in as $userType with PIN: $pin",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                Toast.makeText(context, "Please enter a 4-digit PIN", Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.switchToCredentials.setOnClickListener {
            switchLoginMethodListener?.invoke()
        }
    }

    private fun createTextWatcher(nextField: EditText?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1 && nextField != null) {
                    nextField.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    fun setOnSwitchLoginMethodListener(listener: () -> Unit) {
        switchLoginMethodListener = listener
    }
}