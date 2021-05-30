package com.abproject.tsp_cart.model.repository

import android.content.Context
import android.content.SharedPreferences
import com.abproject.tsp_cart.model.database.dao.UserDao
import com.abproject.tsp_cart.model.dataclass.ApplicationData
import com.abproject.tsp_cart.model.dataclass.User
import com.abproject.tsp_cart.util.EncryptionTools
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_ADMIN
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_USER
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_USERNAME
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences,
) : AuthRepository {

    override suspend fun insertUser(user: User): Boolean {
        val checkUsernameIsExist = searchInUsersByUsername(user.username)
        if (checkUsernameIsExist != null)
            return false

        val encryptedPassword = EncryptionTools(context).encryptRSA(user.password)
        user.password = encryptedPassword
        userDao.insertUser(user)

        saveApplicationDataInSharedPrefs(
            username = user.username,
            isAdmin = user.isManager,
            isUser = !user.isManager
        )
        loadApplicationData(
            username = user.username,
            isAdmin = user.isManager,
            isUser = !user.isManager
        )
        return true
    }

    override suspend fun searchInUsersByUsername(query: String): User? {
        return userDao.searchInUsersByUsername(query)
    }

    override fun loadApplicationData(
        username: String?,
        isAdmin: Boolean,
        isUser: Boolean,
    ) {
        username?.let {
            ApplicationData.setUsername(it)
        }
        ApplicationData.setUserOrAdmin(
            isUser,
            isAdmin
        )
    }

    override fun loadApplicationDataFromSharedPrefs() {
        ApplicationData.setUserOrAdmin(
            isUser = sharedPreferences.getBoolean(
                SHARED_KEY_USER, false
            ),
            isAdmin = sharedPreferences.getBoolean(
                SHARED_KEY_ADMIN, false
            )
        )
        ApplicationData.setUsername(
            notNullStringFromSharedPrefs(
                sharedPreferences,
                SHARED_KEY_USERNAME
            )
        )
    }

    override fun saveApplicationDataInSharedPrefs(
        username: String?,
        isAdmin: Boolean,
        isUser: Boolean,
    ) {
        sharedPreferences.edit().apply {
            putString(SHARED_KEY_USERNAME, username)
            putBoolean(SHARED_KEY_ADMIN, isAdmin)
            putBoolean(SHARED_KEY_USER, isUser)
        }.apply()
    }

    override fun clearDataFromObjectAndSharedPrefs() {
        ApplicationData.clearApplicationData()
        sharedPreferences
            .edit()
            .clear()
            .apply()
    }

    override fun checkDataFromShared(): Boolean {
        val username = sharedPreferences.getString(SHARED_KEY_USERNAME, null)
        return username != null && username.isNotEmpty()
    }

    override fun checkAdminPanelFromShared(): Boolean {
        val isAdmin = sharedPreferences.getBoolean(SHARED_KEY_ADMIN, false)
        val isUser = sharedPreferences.getBoolean(SHARED_KEY_USER, false)
        return isAdmin && !isUser
    }

    /**
     * it's a custom function for sharedPrefs getString method
     * getString @return null if that key doesn't exist, this function
     * take that null and return an empty string if it's null.
     */
    private fun notNullStringFromSharedPrefs(
        sharedPreferences: SharedPreferences,
        key: String,
        defaultValue: String? = null,
    ): String {
        val response = sharedPreferences.getString(key, defaultValue)
        return response ?: ""
    }

}