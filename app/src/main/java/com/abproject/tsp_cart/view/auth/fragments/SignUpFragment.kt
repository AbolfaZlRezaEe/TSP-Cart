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
import com.abproject.tsp_cart.databinding.FragmentAuthSectionBinding
import com.abproject.tsp_cart.databinding.FragmentSigninBinding
import com.abproject.tsp_cart.databinding.FragmentSignupBinding
import com.abproject.tsp_cart.databinding.FragmentWelcomeBinding
import com.abproject.tsp_cart.util.DateConverter
import com.abproject.tsp_cart.util.Resource
import com.abproject.tsp_cart.util.Variables
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_ADMIN_LOGIN
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_USER_LOGIN
import com.abproject.tsp_cart.util.checkEmailIsValid
import com.abproject.tsp_cart.view.auth.AuthViewModel
import com.abproject.tsp_cart.view.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : TSPFragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding =
            FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressCallBack)
        binding.backButtonSignUp.setOnClickListener { findNavController().popBackStack() }
        getObserver()
        initializeSignUp()
    }

    private fun getObserver() {
        authViewModel.saveUserInformationResult.observe(viewLifecycleOwner) { response ->
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

    private fun initializeSignUp() {
        binding.signUpButton.setOnClickListener {
            if (validationEditTexts()) {
                authViewModel.saveUserInformation(
                    binding.fullNameSignUpEditText.text.toString(),
                    binding.emailSignUpEditText.text.toString(),
                    binding.usernameSignUpEditText.text.toString(),
                    binding.passwordSignUpEditText.text.toString(),
                    isAdmin(),
                    DateConverter.convertDateToString(DateConverter.provideDate())
                )
            } else
                setErrorForEditTexts()
        }
    }

    private fun validationEditTexts(): Boolean {
        return binding.fullNameSignUpEditText.text.isNotEmpty()
                && binding.emailSignUpEditText.text.isNotEmpty()
                && binding.emailSignUpEditText.text.checkEmailIsValid()
                && binding.usernameSignUpEditText.text.isNotEmpty()
                && binding.passwordSignUpEditText.text.isNotEmpty()
    }

    private fun setErrorForEditTexts() {
        if (binding.fullNameSignUpEditText.text.isEmpty())
            binding.fullNameSignUpEditText.error = getString(R.string.fullNameError)
        if (binding.emailSignUpEditText.text.isEmpty())
            binding.emailSignUpEditText.error = getString(R.string.emailError)
        if (!binding.emailSignUpEditText.text.checkEmailIsValid())
            binding.emailSignUpEditText.error = getString(R.string.wrongEmail)
        if (binding.usernameSignUpEditText.text.isEmpty())
            binding.usernameSignUpEditText.error = getString(R.string.usernameError)
        if (binding.passwordSignUpEditText.text.isEmpty())
            binding.passwordSignUpEditText.error = getString(R.string.password)
    }

    private val onBackPressCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    }

    private fun isAdmin(): Boolean {
        val adminArgument = arguments?.getBoolean(Variables.KEY_ADMIN)
        return adminArgument != null && adminArgument == true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}