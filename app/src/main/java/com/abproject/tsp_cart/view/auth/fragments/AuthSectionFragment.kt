package com.abproject.tsp_cart.view.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.base.TSPFragment
import com.abproject.tsp_cart.databinding.FragmentAuthSectionBinding
import com.abproject.tsp_cart.util.Variables.KEY_ADMIN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthSectionFragment : TSPFragment() {

    private var _binding: FragmentAuthSectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding =
            FragmentAuthSectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeTitleFragment()

        binding.signInSectionWelcomeButton.setOnClickListener {
            val bundle = bundleOf(KEY_ADMIN to isAdmin())
            findNavController().navigate(R.id.action_authSectionFragment_to_signInFragment,
                bundle)
        }
        binding.signUpSectionWelcomeButton.setOnClickListener {
            val bundle = bundleOf(KEY_ADMIN to isAdmin())
            findNavController().navigate(R.id.action_authSectionFragment_to_signUpFragment,
                bundle)
        }
    }

    private fun isAdmin(): Boolean {
        val adminArgument = arguments?.getBoolean(KEY_ADMIN)
        return adminArgument != null && adminArgument == true
    }

    private fun initializeTitleFragment() {
        if (isAdmin())
            binding.titleTextAuthSection.text = getString(R.string.adminPanel)
        else
            binding.titleTextAuthSection.text = getString(R.string.userPanel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}