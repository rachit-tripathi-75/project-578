package com.example.bankingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bankingapp.R
import com.example.bankingapp.databinding.FragmentLoginContainerBinding
import com.example.bankingapp.databinding.FragmentUserLoginContainerBinding


class UserLoginContainerFragment : Fragment() {

    private var usePin = false


    private lateinit var binding: FragmentLoginContainerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginContainerBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initially show credential login
        showLoginMethod(false)
    }

    private fun showLoginMethod(usePin: Boolean) {
        this.usePin = usePin
        val transaction = childFragmentManager.beginTransaction()

        val fragment = if (usePin) {
            PinLoginFragment.newInstance("user")
        } else {
            CredentialLoginFragment.newInstance("user")
        }

        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()

        // Set up communication between fragments
        if (usePin) {
            (fragment as PinLoginFragment).setOnSwitchLoginMethodListener {
                showLoginMethod(false)
            }
        } else {
            (fragment as CredentialLoginFragment).setOnSwitchLoginMethodListener {
                showLoginMethod(true)
            }
        }
    }


}