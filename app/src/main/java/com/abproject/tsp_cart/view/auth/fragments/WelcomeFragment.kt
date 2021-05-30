package com.abproject.tsp_cart.view.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.base.TSPFragment
import com.abproject.tsp_cart.databinding.FragmentWelcomeBinding
import com.abproject.tsp_cart.util.Variables.KEY_ADMIN

class WelcomeFragment : TSPFragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding =
            FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressCallBack)

        binding.adminWelcomeButton.setOnClickListener {
            val bundle = bundleOf(KEY_ADMIN to true)
            findNavController().navigate(R.id.action_welcomeFragment_to_authSectionFragment, bundle)
        }

        binding.userWelcomeButton.setOnClickListener {
            val bundle = bundleOf(KEY_ADMIN to false)
            findNavController().navigate(R.id.action_welcomeFragment_to_authSectionFragment, bundle)
        }
    }

    val onBackPressCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}