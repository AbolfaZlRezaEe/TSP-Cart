package com.abproject.tsp_cart.model.repository.auth

import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.dataclass.User
import com.abproject.tsp_cart.util.Resource

interface AuthRepository {

    suspend fun insertUser(
        user: User,
    ): Boolean

    suspend fun updateUser(
        user: User,
    )

    suspend fun checkUserExisting(
        username: String,
        password: String,
        isAdmin: Boolean,
    ): LiveData<Resource<Boolean>>

    fun checkDataIsExist(): Boolean

    fun checkUserDataIsAdmin(): Boolean

    suspend fun getUserInformation(): User?
}