package com.example.bankingapp.agent.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bankingapp.R
import com.example.bankingapp.agent.HomeActivity
import com.example.bankingapp.classes.ApiClient
import com.example.bankingapp.classes.PrefsManager
import com.example.bankingapp.databinding.ActivityCreateMpinAgentBinding
import com.example.bankingapp.databinding.ActivityCreateMpinBinding
import com.example.bankingapp.databinding.FragmentCreatePinBinding
import com.example.bankingapp.responses.CreateMPinResponse
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import kotlin.collections.forEach

class CreateMPinActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: MPinPagerAdapter
    private var createdPin: String = ""

    private lateinit var binding: ActivityCreateMpinAgentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateMpinAgentBinding.inflate(layoutInflater)
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

        pagerAdapter = MPinPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        // Disable swiping between pages
        binding.viewPager.isUserInputEnabled = false
    }

    private fun listeners() {

    }



    fun navigateToConfirmPin(pin: String) {
        createdPin = pin
        binding.viewPager.currentItem = 1
    }

    fun navigateBackToCreatePin() {
        binding.viewPager.currentItem = 0
    }

    fun navigateToSuccess() {
        binding.viewPager.currentItem = 2

        insertMPinToDatabase()

        // In a real app, you would save the PIN securely here
        // For example: savePin(createdPin)
    }

    fun getCreatedPin(): String {
        return createdPin
    }


    private fun insertMPinToDatabase() {

        ApiClient.createMPinInstance.createMPin(
            "Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
            "ci_session=r4gbb0fffqo5r9tdn89kukeq35cpan3c",
            getCreatedPin(),
            getAndroidId(this),
            PrefsManager.getUserInformation(this).data.memId).enqueue(object : retrofit2.Callback<CreateMPinResponse> {
            override fun onResponse(call: Call<CreateMPinResponse>, response: retrofit2.Response<CreateMPinResponse>) {

                if (response.isSuccessful) {
//                    val gson = Gson()
//                    Log.d("createMPinTAG", "response: ${gson.toJson(response)}")
                    val s = response.body()
                    if (s?.status == 1) {
                        Toast.makeText(applicationContext, "Pin has been saved securely.", Toast.LENGTH_SHORT).show()
                        PrefsManager.setCreatedmPinFlag(applicationContext, true)
                    }

                } else {
                    Toast.makeText(applicationContext, "An Error occurred on pushing to database", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CreateMPinResponse>, t: Throwable) {
                Log.d("createMPinTAG", "Error: ${t.message}")
                Toast.makeText(applicationContext, "An error has occurred. Please try again later", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getAndroidId(context: Context): String {
        Log.d("androidIdTAG", Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }



    private inner class MPinPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CreatePinFragment()
                1 -> ConfirmPinFragment()
                2 -> PinSuccessFragment()
                else -> CreatePinFragment()
            }
        }
    }
}

class CreatePinFragment : Fragment() {
    private lateinit var pinDigits: List<EditText>
    private lateinit var errorText: TextView
    private lateinit var weakPinText: TextView
    private lateinit var continueButton: MaterialButton
    private lateinit var showPinContainer: LinearLayout
    private lateinit var showPinIcon: ImageView
    private lateinit var showPinText: TextView
    private lateinit var strengthIndicator: LinearLayout
    private lateinit var strengthBar1: View
    private lateinit var strengthBar2: View
    private lateinit var strengthBar3: View
    private lateinit var strengthText: TextView
    private lateinit var iconContainer: CardView
    private lateinit var shieldIcon: ImageView

    private lateinit var binding: FragmentCreatePinBinding

    private var showPin = false
    private var pinStrength: PinStrength? = null

    enum class PinStrength {
        WEAK, MEDIUM, STRONG
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        pinDigits = listOf(
            view.findViewById(R.id.pinDigit1),
            view.findViewById(R.id.pinDigit2),
            view.findViewById(R.id.pinDigit3),
            view.findViewById(R.id.pinDigit4)
        )
        errorText = view.findViewById(R.id.errorText)
        weakPinText = view.findViewById(R.id.weakPinText)
        continueButton = view.findViewById(R.id.continueButton)
        showPinContainer = view.findViewById(R.id.showPinContainer)
        showPinIcon = view.findViewById(R.id.showPinIcon)
        showPinText = view.findViewById(R.id.showPinText)
        strengthIndicator = view.findViewById(R.id.strengthIndicator)
        strengthBar1 = view.findViewById(R.id.strengthBar1)
        strengthBar2 = view.findViewById(R.id.strengthBar2)
        strengthBar3 = view.findViewById(R.id.strengthBar3)
        strengthText = view.findViewById(R.id.strengthText)
        iconContainer = view.findViewById(R.id.iconContainer)
        shieldIcon = view.findViewById(R.id.shieldIcon)

        // Set up PIN input fields
        setupPinInputs()

        // Set up continue button
        setupContinueButton()

        // Set up show PIN toggle
        setupShowPinToggle()

        // Animate the shield icon
        animateShieldIcon()
    }

    private fun setupPinInputs() {
        // Focus on first digit initially
        pinDigits[0].requestFocus()

        // Set up text watchers for each PIN digit
        pinDigits.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: android.text.Editable?) {
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

                    // Check PIN strength
                    checkPinStrength()

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

    private fun checkPinStrength() {
        // Only check strength if all digits are filled
        if (pinDigits.all { it.text.isNotEmpty() }) {
            val pin = getPinString()

            // Check for sequential digits (e.g., 123456)
            val isSequential = Regex("^(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){5}\\d$").matches(pin)

            // Check for repeated digits (e.g., 111111, 222222)
            val isRepeated = Regex("^(\\d)\\1{5}$").matches(pin)

            // Check for common PINs
            val isCommon = listOf("123456", "654321", "111111", "222222", "333333", "444444", "555555",
                "666666", "777777", "888888", "999999", "000000").contains(pin)

            if (isSequential || isRepeated || isCommon) {
                setPinStrength(PinStrength.WEAK)
                weakPinText.visibility = View.VISIBLE
            } else {
                // Check for partially repeated patterns
                val hasRepeatedPattern = Regex("(\\d{2,})\\1+").matches(pin)

                if (hasRepeatedPattern) {
                    setPinStrength(PinStrength.MEDIUM)
                } else {
                    setPinStrength(PinStrength.STRONG)
                }
                weakPinText.visibility = View.GONE
            }

            strengthIndicator.visibility = View.VISIBLE
        } else {
            pinStrength = null
            strengthIndicator.visibility = View.GONE
            weakPinText.visibility = View.GONE
        }
    }

    private fun setPinStrength(strength: PinStrength) {
        pinStrength = strength

        when (strength) {
            PinStrength.WEAK -> {
                strengthBar1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red_500))
                strengthBar2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_200))
                strengthBar3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_200))
                strengthText.text = "Weak PIN"
                strengthText.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_500))
            }
            PinStrength.MEDIUM -> {
                strengthBar1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow_500))
                strengthBar2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow_500))
                strengthBar3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_200))
                strengthText.text = "Medium PIN"
                strengthText.setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow_700))
            }
            PinStrength.STRONG -> {
                strengthBar1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_500))
                strengthBar2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_500))
                strengthBar3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_500))
                strengthText.text = "Strong PIN"
                strengthText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_600))
            }
        }
    }

    private fun updateContinueButtonState() {
        // Enable continue button only if all digits are filled
        continueButton.isEnabled = pinDigits.all { it.text.isNotEmpty() }

        if (continueButton.isEnabled) {
            // Add a subtle animation to the button when it becomes enabled
            val animation = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.pulse)
            continueButton.startAnimation(animation)
        }
    }

    private fun setupContinueButton() {
        continueButton.isEnabled = false

        continueButton.setOnClickListener {
            val pin = getPinString()

            if (pin.length < 4) {
                showError("Please enter all 4 digits")
                return@setOnClickListener
            }

            if (pinStrength == PinStrength.WEAK) {
                showError("Please choose a stronger PIN")
                return@setOnClickListener
            }

            // Navigate to confirm PIN screen
            (activity as CreateMPinActivity).navigateToConfirmPin(pin)
        }
    }

    private fun setupShowPinToggle() {
        showPinContainer.setOnClickListener {
            showPin = !showPin

            if (showPin) {
                // Show PIN
                pinDigits.forEach { it.inputType = InputType.TYPE_CLASS_NUMBER }
                showPinIcon.setImageResource(R.drawable.ic_visibility_off)
                showPinText.text = "Hide PIN"
            } else {
                // Hide PIN
                pinDigits.forEach { it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD }
                showPinIcon.setImageResource(R.drawable.ic_visibility)
                showPinText.text = "Show PIN"
            }
        }
    }

    private fun showError(message: String) {
        errorText.text = message
        errorText.visibility = View.VISIBLE

        // Shake animation for error
        val animation = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        errorText.startAnimation(animation)

        // Also shake the PIN container
        val pinContainer = view?.findViewById<View>(R.id.pinContainer)
        pinContainer?.startAnimation(animation)
    }

    private fun getPinString(): String {
        return pinDigits.joinToString("") { it.text.toString() }
    }

    private fun animateShieldIcon() {
        // Add a subtle pulse animation to the shield icon
        val scaleX = ObjectAnimator.ofFloat(iconContainer, "scaleX", 1f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(iconContainer, "scaleY", 1f, 1.1f, 1f)
        val rotate = ObjectAnimator.ofFloat(shieldIcon, "rotation", 0f, 5f, -5f, 0f)

        val animSet = AnimatorSet()
        animSet.playTogether(scaleX, scaleY, rotate)
        animSet.duration = 1000
        animSet.startDelay = 500
        animSet.start()
    }
}

class ConfirmPinFragment : Fragment() {
    private lateinit var pinDigits: List<EditText>
    private lateinit var errorText: TextView
    private lateinit var backButton: MaterialButton
    private lateinit var createPinButton: MaterialButton
    private lateinit var showPinContainer: LinearLayout
    private lateinit var showPinIcon: ImageView
    private lateinit var showPinText: TextView
    private lateinit var iconContainer: CardView
    private lateinit var shieldIcon: ImageView

    private var showPin = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        pinDigits = listOf(
            view.findViewById(R.id.pinDigit1),
            view.findViewById(R.id.pinDigit2),
            view.findViewById(R.id.pinDigit3),
            view.findViewById(R.id.pinDigit4)
        )
        errorText = view.findViewById(R.id.errorText)
        backButton = view.findViewById(R.id.backButton)
        createPinButton = view.findViewById(R.id.createPinButton)
        showPinContainer = view.findViewById(R.id.showPinContainer)
        showPinIcon = view.findViewById(R.id.showPinIcon)
        showPinText = view.findViewById(R.id.showPinText)
        iconContainer = view.findViewById(R.id.iconContainer)
        shieldIcon = view.findViewById(R.id.shieldIcon)

        // Set up PIN input fields
        setupPinInputs()

        // Set up buttons
        setupButtons()

        // Set up show PIN toggle
        setupShowPinToggle()

        // Animate the shield icon
        animateShieldIcon()

        // Focus on first digit
        pinDigits[0].requestFocus()
    }

    private fun setupPinInputs() {
        // Set up text watchers for each PIN digit
        pinDigits.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: android.text.Editable?) {
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

                    // Update create PIN button state
                    updateCreatePinButtonState()
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

    private fun updateCreatePinButtonState() {
        // Enable create PIN button only if all digits are filled
        createPinButton.isEnabled = pinDigits.all { it.text.isNotEmpty() }

        if (createPinButton.isEnabled) {
            // Add a subtle animation to the button when it becomes enabled
            val animation = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.pulse)
            createPinButton.startAnimation(animation)
        }
    }

    private fun setupButtons() {
        createPinButton.isEnabled = false

        backButton.setOnClickListener {
            // Navigate back to create PIN screen
            (activity as CreateMPinActivity).navigateBackToCreatePin()
        }

        createPinButton.setOnClickListener {
            val confirmPin = getPinString()
            val originalPin = (activity as CreateMPinActivity).getCreatedPin()

            if (confirmPin.length < 4) {
                showError("Please enter all 4 digits")
                return@setOnClickListener
            }

            if (confirmPin != originalPin) {
                showError("PINs don't match. Please try again.")
                clearPinFields()
                return@setOnClickListener
            }

            // Navigate to success screen
            (activity as CreateMPinActivity).navigateToSuccess()
        }
    }

    private fun setupShowPinToggle() {
        showPinContainer.setOnClickListener {
            showPin = !showPin

            if (showPin) {
                // Show PIN
                pinDigits.forEach { it.inputType = InputType.TYPE_CLASS_NUMBER }
                showPinIcon.setImageResource(R.drawable.ic_visibility_off)
                showPinText.text = "Hide PIN"
            } else {
                // Hide PIN
                pinDigits.forEach { it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD }
                showPinIcon.setImageResource(R.drawable.ic_visibility)
                showPinText.text = "Show PIN"
            }
        }
    }

    private fun showError(message: String) {
        errorText.text = message
        errorText.visibility = View.VISIBLE

        // Shake animation for error
        val animation = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        errorText.startAnimation(animation)

        // Also shake the PIN container
        val pinContainer = view?.findViewById<View>(R.id.pinContainer)
        pinContainer?.startAnimation(animation)
    }

    private fun clearPinFields() {
        pinDigits.forEach { it.text.clear() }
        pinDigits[0].requestFocus()
    }

    private fun getPinString(): String {
        return pinDigits.joinToString("") { it.text.toString() }
    }

    private fun animateShieldIcon() {
        // Add a subtle pulse animation to the shield icon
        val scaleX = ObjectAnimator.ofFloat(iconContainer, "scaleX", 1f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(iconContainer, "scaleY", 1f, 1.1f, 1f)
        val rotate = ObjectAnimator.ofFloat(shieldIcon, "rotation", 0f, 5f, -5f, 0f)

        val animSet = AnimatorSet()
        animSet.playTogether(scaleX, scaleY, rotate)
        animSet.duration = 1000
        animSet.startDelay = 500
        animSet.start()
    }
}

class PinSuccessFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var continueButton: MaterialButton
    private lateinit var iconContainer: CardView
    private lateinit var checkIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pin_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        progressBar = view.findViewById(R.id.progressBar)
        continueButton = view.findViewById(R.id.continueButton)
        iconContainer = view.findViewById(R.id.iconContainer)
        checkIcon = view.findViewById(R.id.checkIcon)

        // Set up continue button
        setupContinueButton()

        // Animate success elements
        animateSuccessElements()
    }

    private fun setupContinueButton() {
        continueButton.setOnClickListener {
            // In a real app, you would navigate to your home screen activity
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finishAffinity()
            PrefsManager.setCreatedmPinFlag(requireContext(), true)
            PrefsManager.setShouldShowCreatePinBottomSheetDialog(requireContext(), false) // no need to show again the bottom sheet dialog of CREATE-PIN!!!

            // Add a nice transition animation
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            // Finish this activity so user can't go back to PIN screen with back button
        }
    }

    private fun animateSuccessElements() {
        // Animate the check icon
        val scaleX = ObjectAnimator.ofFloat(iconContainer, "scaleX", 0f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(iconContainer, "scaleY", 0f, 1.2f, 1f)
        val rotate = ObjectAnimator.ofFloat(checkIcon, "rotation", 0f, 10f, -10f, 0f)

        val iconAnimSet = AnimatorSet()
        iconAnimSet.playTogether(scaleX, scaleY, rotate)
        iconAnimSet.duration = 500
        iconAnimSet.start()

        // Animate progress bar
        CoroutineScope(Dispatchers.Main).launch {
            delay(300)
            val progressAnim = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
            progressAnim.duration = 1000
            progressAnim.interpolator = AccelerateDecelerateInterpolator()
            progressAnim.start()
        }
    }



}