package com.abproject.tsp_cart.model.repository

import android.content.Context
import com.abproject.tsp_cart.model.database.dao.UserDao
import com.abproject.tsp_cart.model.dataclass.User
import com.abproject.tsp_cart.util.EncryptionTools
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val userDao: UserDao,
) : AuthRepository {

    override suspend fun insertUser(user: User): Boolean {
        val checkUsernameIsExist = searchInUsersByUsername(user.username)
        if (checkUsernameIsExist != null)
            return false

        val encryptedPassword = EncryptionTools(context).encryptRSA(user.password)
        user.password = encryptedPassword
        userDao.insertUser(user)
        return true
    }

    override suspend fun searchInUsersByUsername(query: String): User? {
        return userDao.searchInUsersByUsername(query)
    }
}