package com.example.bankingapp.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bankingapp.fragments.ApplicantFormFragment
import com.example.bankingapp.fragments.CoApplicantFormFragment

class PersonalLoanFormViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ApplicantFormFragment()
            1 -> CoApplicantFormFragment()
            else -> throw IllegalStateException("Unexpected position ${position}")
        }
    }

    override fun getItemCount(): Int = 2
}