package com.example.bankingapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bankingapp.R
import com.example.bankingapp.agent.HomeActivity
import com.example.bankingapp.databinding.FragmentAgentLoginBinding


class AgentLoginFragment : Fragment() {

    private lateinit var binding: FragmentAgentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAgentLoginBinding.inflate(inflater, container, false)
        listeners()
        return binding.root
    }

    private fun listeners() {
        binding.btnSignIn.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), HomeActivity::class.java))
        }
    }


}