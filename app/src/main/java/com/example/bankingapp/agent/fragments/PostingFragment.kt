package com.example.bankingapp.agent.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bankingapp.R
import com.example.bankingapp.databinding.FragmentPostingBinding


class PostingFragment : Fragment() {

    private lateinit var binding: FragmentPostingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPostingBinding.inflate(inflater, container, false)

        initialisers()

        return binding.root
    }

    private fun initialisers() {

    }



}