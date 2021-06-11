package com.abproject.tsp_cart.view.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.base.TSPActivity
import com.abproject.tsp_cart.databinding.ActivityProfileBinding
import com.abproject.tsp_cart.model.dataclass.User
import com.abproject.tsp_cart.util.DateConverter
import com.abproject.tsp_cart.util.Resource
import com.abproject.tsp_cart.util.Variables.REQUEST_CODE_CHOOSE_PROFILE_PICTURES
import com.abproject.tsp_cart.util.checkEmailIsValid
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : TSPActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private var profileImage: String? = null
    private var user: User? = null

    /**
     * This variable indicates the snack bar display mode.
     * because in this activity i use showing multiple
     * snackbar in multiple logic, need a variable for
     * check snackbar is showing now or not!
     */
    private var snackBarIsShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getObservers()
        setupChoosePictureProfileClick()
        profileViewModel.getUserInformation()

        binding.fabUpdateUserInformation.setOnClickListener {
            user?.let {
                setupSaveChangeUserInformation(it)
            }
        }

        binding.backButtonUserInformation.setOnClickListener {
            this.finish()
        }

    }

    private fun getObservers() {
        profileViewModel.getUserInformation.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { user ->
                        setupUi(user)
                        this.user = user
                    }
                }
                is Resource.Error -> {
                    showProgressBar(false)
                    showSnackBar(response.message!!)
                }
            }
        }
        profileViewModel.updateUserInformation.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { isSuccess ->
                        if (isSuccess) {
                            showSnackBar(response.message!!)
                            Handler(Looper.getMainLooper()).postDelayed({
                                this.finish()
                            }, 2000)
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

    private fun setupUi(
        user: User,
    ) {
        if (user.profile != null) {
            showFirstCharOrProfile(false)
            Glide.with(this)
                .load(Uri.parse(user.profile))
                .into(binding.profileImageViewProfile)
        } else {
            showFirstCharOrProfile(true)
            binding.firstCharUserNameProfile.text =
                user.username.first().toString()
        }


        binding.fullNameEditTextProfile.setText(user.fullname)
        binding.usernameEditTextProfile.setText(user.username)
        binding.emailEditTextProfile.setText(user.email)
    }

    private fun setupChoosePictureProfileClick() {
        binding.buttonChooseProfileImage.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .crop()
                .start(REQUEST_CODE_CHOOSE_PROFILE_PICTURES)
        }
    }

    private fun setupSaveChangeUserInformation(
        user: User,
    ) {
        if (validationEditTexts()) {
            if (!nothingChange(user)) {
                user.fullname = binding.fullNameEditTextProfile.text.toString()
                user.username = binding.usernameEditTextProfile.text.toString()
                user.password = checkNewPasswordIsExist(user) ?: user.password
                user.email = binding.emailEditTextProfile.text.toString()
                user.profile = profileImage ?: user.profile
                user.updateAt = DateConverter.convertDateToString(DateConverter.provideDate())
                profileViewModel.updateUser(user)
            } else {
                if (!snackBarIsShowing) {
                    showSnackBar("Please change something or exit in this page!")
                }
            }
        } else {
            setErrorForEditTexts()
        }
    }

    private fun checkNewPasswordIsExist(
        user: User,
    ): String? {
        if (
            binding.newPasswordEditTextProfile.text.isNotEmpty()
            && binding.confirmPasswordEditTextProfile.text.isNotEmpty()
        ) {
            if (
                binding.newPasswordEditTextProfile.text.toString() ==
                binding.confirmPasswordEditTextProfile.text.toString()
            ) {
                if (
                    binding.newPasswordEditTextProfile.text.toString() !=
                    user.password
                ) {
                    return binding.newPasswordEditTextProfile.text.toString()
                } else {
                    snackBarIsShowing = true
                    showSnackBar("Please enter new password!")
                }
            } else {
                snackBarIsShowing = true
                showSnackBar("Please check your password!")
            }
        }
        return null
    }

    private fun passwordIsChange(): Boolean {
        if (
            binding.newPasswordEditTextProfile.text.toString() ==
            binding.confirmPasswordEditTextProfile.text.toString()
            && binding.newPasswordEditTextProfile.text.isNotEmpty()
            && binding.confirmPasswordEditTextProfile.text.isNotEmpty()
        ) {
            return true
        } else if (binding.newPasswordEditTextProfile.text.isNotEmpty()
            && binding.confirmPasswordEditTextProfile.text.isNotEmpty()
            && binding.newPasswordEditTextProfile.text.toString() !=
            binding.confirmPasswordEditTextProfile.text.toString()
        ) {
            snackBarIsShowing = true
            showSnackBar(
                "please check your entered passwords, these passwords is not the same!",
                Snackbar.LENGTH_LONG)
            return false
        } else if (
            binding.newPasswordEditTextProfile.text.isNotEmpty()
            && binding.confirmPasswordEditTextProfile.text.isEmpty()
        ) {
            snackBarIsShowing = true
            showSnackBar(
                "Please enter again password in Confirm Password field!",
                Snackbar.LENGTH_LONG)
            return false
        } else if (
            binding.newPasswordEditTextProfile.text.isEmpty()
            && binding.confirmPasswordEditTextProfile.text.isNotEmpty()
        ) {
            snackBarIsShowing = true
            showSnackBar(
                "Please first enter New Password and then Enter Confirm Password!",
                Snackbar.LENGTH_LONG)
            return false
        } else if (
            binding.newPasswordEditTextProfile.text.isEmpty()
            && binding.confirmPasswordEditTextProfile.text.isEmpty()
        ) {
            return false
        }
        return false
    }

    private fun nothingChange(
        user: User,
    ): Boolean {
        return (binding.fullNameEditTextProfile.text.toString() == user.fullname
                && binding.usernameEditTextProfile.text.toString() == user.username
                && !passwordIsChange()
                && binding.emailEditTextProfile.text.toString() == user.email
                && user.profile == profileImage ?: user.profile)
    }

    private fun validationEditTexts(): Boolean {
        return binding.fullNameEditTextProfile.text.isNotEmpty()
                && binding.emailEditTextProfile.text.isNotEmpty()
                && binding.emailEditTextProfile.text.checkEmailIsValid()
                && binding.usernameEditTextProfile.text.isNotEmpty()
    }

    private fun setErrorForEditTexts() {
        if (binding.fullNameEditTextProfile.text.isEmpty())
            binding.fullNameEditTextProfile.error = getString(R.string.fullNameError)
        if (binding.emailEditTextProfile.text.isEmpty())
            binding.emailEditTextProfile.error = getString(R.string.emailError)
        if (!binding.emailEditTextProfile.text.checkEmailIsValid())
            binding.emailEditTextProfile.error = getString(R.string.wrongEmail)
        if (binding.usernameEditTextProfile.text.isEmpty())
            binding.usernameEditTextProfile.error = getString(R.string.usernameError)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_PROFILE_PICTURES) {
                profileImage = data?.data!!.toString()
                showFirstCharOrProfile(false)
                Glide
                    .with(this)
                    .load(Uri.parse(profileImage))
                    .into(binding.profileImageViewProfile)
            }
        }
    }

    private fun showFirstCharOrProfile(showChar: Boolean) {
        if (showChar) {
            binding.profileImageViewProfile.visibility = View.GONE
            binding.firstCharUserNameProfile.visibility = View.VISIBLE
        } else {
            binding.profileImageViewProfile.visibility = View.VISIBLE
            binding.firstCharUserNameProfile.visibility = View.GONE
        }
    }
}