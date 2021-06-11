package com.abproject.tsp_cart.model.repository

import android.content.Context
import android.content.SharedPreferences
import com.abproject.tsp_cart.model.database.dao.UserDao
import com.abproject.tsp_cart.model.dataclass.User
import com.abproject.tsp_cart.model.dataclass.UserData
import com.abproject.tsp_cart.util.EncryptionTools
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_ADMIN
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_EMAIL
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_FULLNAME
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_PROFILE
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_USER
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_USERNAME
import timber.log.Timber
import javax.inject.Inject

class BaseRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userDao: UserDao,
    private val context: Context,
) : BaseRepository {

    override fun saveUserInformationIntoUserDataClass(
        username: String,
        fullname: String,
        profile: String?,
        email: String,
        isAdmin: Boolean,
        isUser: Boolean,
    ): Boolean {
        clearUserData()
        UserData.setupUserData(
            username = username,
            email = email,
            fullName = fullname,
            profile = profile,
            isAdmin = isAdmin,
            isUser = isUser
        )
        return true
    }

    override fun loadUserDataFromSharedPrefs(): Boolean {
        return try {
            clearUserData()
            UserData.setupUserData(
                username = sharedPreferences.getString(SHARED_KEY_USERNAME, null).toString(),
                email = sharedPreferences.getString(SHARED_KEY_EMAIL, null).toString(),
                profile = sharedPreferences.getString(SHARED_KEY_PROFILE, null).toString(),
                fullName = sharedPreferences.getString(SHARED_KEY_FULLNAME, null).toString(),
                isAdmin = sharedPreferences.getBoolean(SHARED_KEY_ADMIN, false),
                isUser = sharedPreferences.getBoolean(SHARED_KEY_USER, false),
            )
            true
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    override fun saveUserInformationIntoSharedPrefs(
        username: String,
        fullname: String,
        email: String,
        profile: String?,
        isAdmin: Boolean,
        isUser: Boolean,
    ): Boolean {
        clearSharedPrefs()
        sharedPreferences.edit().apply {
            putString(SHARED_KEY_USERNAME, username)
            putString(SHARED_KEY_EMAIL, email)
            putString(SHARED_KEY_PROFILE, profile)
            putString(SHARED_KEY_FULLNAME, fullname)
            putBoolean(SHARED_KEY_USER, isUser)
            putBoolean(SHARED_KEY_ADMIN, isAdmin)
        }.apply()
        return true
    }

    override suspend fun getUserInformationWithUsername(
        username: String,
    ): User? {
        var result = userDao.getUserInformationWithUsername(username)
        return if (result != null) {
            val user = decryptPassword(result)
            user
        } else
            null
    }

    override fun checkUserInformationFromSharedIsExist(): Boolean {
        val username = sharedPreferences.getString(SHARED_KEY_USERNAME, null)
        return username != null && username.isNotEmpty()
    }

    override fun checkInformationFromSharedIsAdminOrUser(): Boolean {
        return sharedPreferences.getBoolean(SHARED_KEY_ADMIN, false)
    }

    override fun encryptPassword(
        user: User,
    ): User {
        user.password = EncryptionTools(context).encryptRSA(user.password)
        return user
    }

    override fun decryptPassword(
        user: User,
    ): User {
        user.password = EncryptionTools(context).decryptRSA(user.password)
        return user
    }

    override fun getStringFromShared(
        key: String,
    ): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun getBooleanFromShared(
        key: String,
    ): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun clearUserData() {
        UserData.clearUserData()
    }

    override fun clearSharedPrefs() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}