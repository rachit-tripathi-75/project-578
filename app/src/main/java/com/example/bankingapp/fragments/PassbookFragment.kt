package com.example.bankingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankingapp.R
import com.example.bankingapp.activities.MyPassbookActivity
import com.example.bankingapp.databinding.FragmentPassbookBinding
import java.text.NumberFormat
import java.util.Locale


class PassbookFragment : Fragment() {



    private lateinit var binding: FragmentPassbookBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPassbookBinding.inflate(inflater, container, false)

        initialisers()
        listeners()

        return binding.root

    }


    private fun initialisers() {

    }

    private fun listeners() {

    }






}