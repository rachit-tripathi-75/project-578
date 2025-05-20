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
    private var whereToGo: Int = -1 // this variable will decide, ki konsi screen se a rahe ho? User se ya Agent se...
//    private lateinit var sharedPreferences: SharedPreferences

    companion object {
//        const val PREF_NAME = "pin_creation_prefs"
//        const val KEY_DO_NOT_SHOW = "do_not_show_pin_dialog"
//        const val KEY_PIN_CREATED = "pin_created"
        private const val ARG_WHERE_TO_GO = "where_to_go"

        fun newInstance(whereToGoValue: Int): PinCreationBottomSheet {
            val fragment = PinCreationBottomSheet()
            val args = Bundle().apply {
                putInt(ARG_WHERE_TO_GO, whereToGoValue)
            }
            fragment.arguments = args
            return fragment
        }

//        fun shouldShowDialog(context: Context): Boolean {
//            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            val doNotShow = prefs.getBoolean(KEY_DO_NOT_SHOW, false)
//            val pinCreated = prefs.getBoolean(KEY_PIN_CREATED, false)
//            return !doNotShow && !pinCreated
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Read the argument in onCreate
        arguments?.let {
            whereToGo = it.getInt(ARG_WHERE_TO_GO, -1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetPinCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Apply animations
        val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        view.startAnimation(slideUp)

        binding.btnSkip.setOnClickListener {
            if (binding.cbDoNotShow.isChecked) {
                PrefsManager.setShouldShowCreatePinBottomSheetDialog(requireContext(), false)
            }
            dismiss()
        }

        binding.btnCreatePin.setOnClickListener {
            Log.d("btnCreatePin", "Touched")
            if (whereToGo == 0) { // go to user's CreateMPinActivity page.......
                val intent = Intent(requireContext(), CreateMPinActivity::class.java)
                startActivity(intent)
            } else { // go to agent's CreateMPinActivity page.........
                val intent = Intent(requireContext(), com.example.bankingapp.agent.activities.CreateMPinActivity::class.java)
                startActivity(intent)
            }


        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

//    fun saveDoNotShowPreference(doNotShow: Boolean) {
//        sharedPreferences.edit().putBoolean(KEY_DO_NOT_SHOW, doNotShow).apply()
//    }
}