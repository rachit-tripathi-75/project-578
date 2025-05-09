package com.example.bankingapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bankingapp.fragments.BalanceInfoFragment
import com.example.bankingapp.fragments.DebitCardInfoFragment

class AccountInformationViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DebitCardInfoFragment()
            1 -> BalanceInfoFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}