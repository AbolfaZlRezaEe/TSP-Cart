package com.abproject.tsp_cart.view.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.base.TSPFragment
import com.abproject.tsp_cart.databinding.FragmentSigninBinding
import com.abproject.tsp_cart.databinding.FragmentWelcomeBinding
import com.abproject.tsp_cart.util.Resource
import com.abproject.tsp_cart.util.Variables
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_ADMIN_LOGIN
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_USER_LOGIN
import com.abproject.tsp_cart.view.auth.AuthViewModel
import com.abproject.tsp_cart.view.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : TSPFragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding =
            FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeSignIn()
        getObserver()
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressCallBack)
        binding.backButtonSignIn.setOnClickListener { findNavController().popBackStack() }

    }

    private fun getObserver() {
        authViewModel.userExistingResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { isSuccess ->
                        if (isSuccess) {
                            if (isAdmin()) {
                                startActivity(Intent(requireActivity(),
                                    SplashActivity::class.java).apply {
                                    putExtra(EXTRA_KEY_ADMIN_LOGIN, true)
                                })
                                requireActivity().finish()
                            } else {
                                startActivity(Intent(requireActivity(),
                                    SplashActivity::class.java).apply {
                                    putExtra(EXTRA_KEY_USER_LOGIN, true)
                                })
                                requireActivity().finish()
                            }
                        }
                    }

                }
                is Resource.Error -> {
                    showProgressBar(false)
                    showSnackBar(response.message!!)
                }
            }
        }
    }

    private fun initializeSignIn() {
        binding.signInButton.setOnClickListener {
            if (validationEditTexts()) {
                authViewModel.checkUserInformation(
                    binding.usernameSignInEditText.text.toString(),
                    binding.passwordSignInEditText.text.toString(),
                    requireContext()
                )
            } else
                setErrorForEditTexts()
        }
    }

    private fun setErrorForEditTexts() {
        if (binding.usernameSignInEditText.text.isEmpty())
            binding.usernameSignInEditText.error = getString(R.string.usernameError)
        if (binding.passwordSignInEditText.text.isEmpty())
            binding.passwordSignInEditText.error = getString(R.string.passwordError)
    }

    private fun validationEditTexts(): Boolean {
        return binding.usernameSignInEditText.text.isNotEmpty()
                && binding.passwordSignInEditText.text.isNotEmpty()
    }

    private val onBackPressCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isAdmin(): Boolean {
        val adminArgument = arguments?.getBoolean(Variables.KEY_ADMIN)
        return adminArgument != null && adminArgument == true
    }
}