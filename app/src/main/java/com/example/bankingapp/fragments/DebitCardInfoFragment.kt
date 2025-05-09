package com.example.bankingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bankingapp.R
import com.example.bankingapp.databinding.FragmentDebitCardInfoBinding


class DebitCardInfoFragment : Fragment() {

    private lateinit var binding: FragmentDebitCardInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDebitCardInfoBinding.inflate(inflater, container, false)
        return binding.root
    }


}