package com.example.bankingapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bankingapp.fragments.HomeLoanDocumentsTabFragment
import com.example.bankingapp.fragments.HomeLoanPersonalTabFragment
import com.example.bankingapp.fragments.HomeLoanPropertyTabFragment

class HomeLoanPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeLoanPersonalTabFragment()
            1 -> HomeLoanPropertyTabFragment()
            2 -> HomeLoanDocumentsTabFragment()
            else -> HomeLoanPersonalTabFragment()
        }
    }
}