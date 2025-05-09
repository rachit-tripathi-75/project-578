package com.example.bankingapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bankingapp.fragments.AgentLoginFragment
import com.example.bankingapp.fragments.UserLoginFragment



class LoginPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) UserLoginFragment() else AgentLoginFragment()
    }
}