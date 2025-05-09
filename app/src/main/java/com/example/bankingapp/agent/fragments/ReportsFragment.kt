package com.example.bankingapp.agent.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import com.example.bankingapp.R
import com.example.bankingapp.agent.activities.AccountStatementActivity
import com.example.bankingapp.databinding.FragmentReportsBinding


class ReportsFragment : Fragment() {
    private lateinit var options: ActivityOptionsCompat
    private lateinit var binding: FragmentReportsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReportsBinding.inflate(inflater, container, false)

        initialisers()
        listeners()

        return binding.root
    }

    private fun initialisers() {
        options = ActivityOptionsCompat.makeCustomAnimation(requireContext(), R.anim.fade_in, R.anim.fade_out);
    }

    private fun listeners() {


    }


}