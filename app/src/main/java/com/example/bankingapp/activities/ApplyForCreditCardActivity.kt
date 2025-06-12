package com.example.bankingapp.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bankingapp.R
import com.example.bankingapp.databinding.ActivityApplyForCreditCardBinding

class ApplyForCreditCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApplyForCreditCardBinding
    private var currentStep = 1
    private val totalSteps = 4
    private var selectedCardType = ""
    private val formData = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityApplyForCreditCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
        loadStep(currentStep)

    }



    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            if (currentStep > 1) {
                currentStep--
                loadStep(currentStep)
            }
        }

        binding.btnContinue.setOnClickListener {
            if (validateCurrentStep()) {
                if (currentStep < totalSteps) {
                    currentStep++
                    loadStep(currentStep)
                } else {
                    submitApplication()
                }
            }
        }
    }

    private fun loadStep(step: Int) {
        updateHeader()
        updateProgressBar()

        when (step) {
            1 -> loadCardSelectionStep()
            2 -> loadPersonalInfoStep()
            3 -> loadAddressEmploymentStep()
            4 -> loadDocumentVerificationStep()
        }

        updateContinueButton()
    }

    private fun updateHeader() {
        binding.tvStepIndicator.text = "Step $currentStep of $totalSteps"
        binding.btnBack.visibility = if (currentStep > 1) View.VISIBLE else View.GONE
    }

    private fun updateProgressBar() {
        val progress = (currentStep * 100) / totalSteps
        binding.progressBar.progress = progress
    }

    private fun loadCardSelectionStep() {
        val stepView = layoutInflater.inflate(R.layout.step_card_selection, null)
        binding.contentContainer.removeAllViews()
        binding.contentContainer.addView(stepView)

        val cardPlatinum = stepView.findViewById<LinearLayout>(R.id.cardPlatinum)
        val cardGold = stepView.findViewById<LinearLayout>(R.id.cardGold)
        val cardTitanium = stepView.findViewById<LinearLayout>(R.id.cardTitanium)

        val checkPlatinum = stepView.findViewById<ImageView>(R.id.checkPlatinum)
        val checkGold = stepView.findViewById<ImageView>(R.id.checkGold)
        val checkTitanium = stepView.findViewById<ImageView>(R.id.checkTitanium)

        cardPlatinum.setOnClickListener {
            selectCard("platinum", cardPlatinum, checkPlatinum,
                listOf(cardGold, cardTitanium), listOf(checkGold, checkTitanium))
        }

        cardGold.setOnClickListener {
            selectCard("gold", cardGold, checkGold,
                listOf(cardPlatinum, cardTitanium), listOf(checkPlatinum, checkTitanium))
        }

        cardTitanium.setOnClickListener {
            selectCard("titanium", cardTitanium, checkTitanium,
                listOf(cardPlatinum, cardGold), listOf(checkPlatinum, checkGold))
        }

        // Restore selection if exists
        when (selectedCardType) {
            "platinum" -> selectCard("platinum", cardPlatinum, checkPlatinum,
                listOf(cardGold, cardTitanium), listOf(checkGold, checkTitanium))
            "gold" -> selectCard("gold", cardGold, checkGold,
                listOf(cardPlatinum, cardTitanium), listOf(checkPlatinum, checkTitanium))
            "titanium" -> selectCard("titanium", cardTitanium, checkTitanium,
                listOf(cardPlatinum, cardGold), listOf(checkPlatinum, checkGold))
        }
    }

    private fun selectCard(cardType: String, selectedCard: LinearLayout, selectedCheck: ImageView,
                           otherCards: List<LinearLayout>, otherChecks: List<ImageView>) {
        selectedCardType = cardType

        // Update selected card
        selectedCard.background = ContextCompat.getDrawable(this, R.drawable.card_selected)
        selectedCheck.visibility = View.VISIBLE

        // Update other cards
        otherCards.forEach { card ->
            card.background = ContextCompat.getDrawable(this, R.drawable.card_background)
        }
        otherChecks.forEach { check ->
            check.visibility = View.GONE
        }

        updateContinueButton()
    }

    private fun loadPersonalInfoStep() {
        val stepView = layoutInflater.inflate(R.layout.step_personal_info, null)
        binding.contentContainer.removeAllViews()
        binding.contentContainer.addView(stepView)

        // Setup input fields with saved data
        val etFullName = stepView.findViewById<EditText>(R.id.etFullName)
        val etEmail = stepView.findViewById<EditText>(R.id.etEmail)
        val etPhone = stepView.findViewById<EditText>(R.id.etPhone)
        val etDateOfBirth = stepView.findViewById<EditText>(R.id.etDateOfBirth)

        etFullName.setText(formData["fullName"] ?: "")
        etEmail.setText(formData["email"] ?: "")
        etPhone.setText(formData["phone"] ?: "")
        etDateOfBirth.setText(formData["dateOfBirth"] ?: "")

        // Add text watchers to save data
        etFullName.addTextChangedListener(createTextWatcher("fullName"))
        etEmail.addTextChangedListener(createTextWatcher("email"))
        etPhone.addTextChangedListener(createTextWatcher("phone"))
        etDateOfBirth.addTextChangedListener(createTextWatcher("dateOfBirth"))
    }

    private fun loadAddressEmploymentStep() {
        val stepView = layoutInflater.inflate(R.layout.step_address_employment, null)
        binding.contentContainer.removeAllViews()
        binding.contentContainer.addView(stepView)

        // Setup input fields and spinners
        // Similar implementation as personal info step
    }

    private fun loadDocumentVerificationStep() {
        val stepView = layoutInflater.inflate(R.layout.step_document_verification, null)
        binding.contentContainer.removeAllViews()
        binding.contentContainer.addView(stepView)

        // Setup document input fields and checkboxes
        // Similar implementation as other steps
    }

    private fun createTextWatcher(key: String): android.text.TextWatcher {
        return object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                formData[key] = s.toString()
                updateContinueButton()
            }
        }
    }

    private fun validateCurrentStep(): Boolean {
        return when (currentStep) {
            1 -> selectedCardType.isNotEmpty()
            2 -> formData["fullName"]?.isNotEmpty() == true &&
                    formData["email"]?.isNotEmpty() == true &&
                    formData["phone"]?.isNotEmpty() == true
            3 -> formData["address"]?.isNotEmpty() == true &&
                    formData["city"]?.isNotEmpty() == true
            4 -> formData["panCard"]?.isNotEmpty() == true &&
                    formData["aadharCard"]?.isNotEmpty() == true
            else -> false
        }
    }

    private fun updateContinueButton() {
        val isValid = validateCurrentStep()
        binding.btnContinue.isEnabled = isValid
        binding.btnContinue.alpha = if (isValid) 1.0f else 0.5f

        binding.btnContinue.text = if (currentStep == totalSteps) "Submit Application" else "Continue"
    }

    private fun submitApplication() {
        // Handle application submission
        Toast.makeText(this, "Application submitted successfully!", Toast.LENGTH_LONG).show()
        // Navigate to success screen or finish activity
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}