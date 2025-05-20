package com.example.bankingapp.classes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.example.bankingapp.R
import com.example.bankingapp.activities.CreateMPinActivitiy
import com.example.bankingapp.activities.CreateMPinActivity
import com.example.bankingapp.databinding.BottomSheetPinCreationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PinCreationBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPinCreationBinding


    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PREF_NAME = "pin_creation_prefs"
        const val KEY_DO_NOT_SHOW = "do_not_show_pin_dialog"
        const val KEY_PIN_CREATED = "pin_created"

        fun newInstance(): PinCreationBottomSheet {
            return PinCreationBottomSheet()
        }

        fun shouldShowDialog(context: Context): Boolean {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val doNotShow = prefs.getBoolean(KEY_DO_NOT_SHOW, false)
            val pinCreated = prefs.getBoolean(KEY_PIN_CREATED, false)

            return !doNotShow && !pinCreated
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetPinCreationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Initialize views


        // Apply animations
        val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        view.startAnimation(slideUp)

        // Set up PIN input listener

        // Set up button listeners
        binding.btnSkip.setOnClickListener {
            if (binding.cbDoNotShow.isChecked) {
                saveDoNotShowPreference(true)
            }
            dismiss()
        }

        binding.btnCreatePin.setOnClickListener {
            Log.d("btnCreatePin", "Touched")
            startActivity(Intent(requireContext(), CreateMPinActivity::class.java))
            dismiss()
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

//        // Initialize button state
//        binding.btnCreatePin.isEnabled = false
    }



    private fun saveDoNotShowPreference(doNotShow: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DO_NOT_SHOW, doNotShow).apply()
    }

    private fun savePin(pin: String) {
        // In a real app, you would want to encrypt this PIN before storing it
        // This is just a simple example
        sharedPreferences.edit()
            .putString("user_pin", pin)
            .putBoolean(KEY_PIN_CREATED, true)
            .apply()
    }
}