package com.example.bankingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.bankingapp.R
import com.example.bankingapp.activities.MyPassbookActivity
import com.example.bankingapp.classes.Account
import com.example.bankingapp.databinding.FragmentDashboardBinding
import java.text.NumberFormat
import java.util.Locale


class DashboardFragment : Fragment() {



    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)


        initialisers()
        listeners()


        return binding.root

    }



    private fun initialisers() {

    }

    private fun listeners() {

    }



}