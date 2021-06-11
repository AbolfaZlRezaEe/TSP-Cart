package com.abproject.tsp_cart.model.repository

import com.abproject.tsp_cart.model.dataclass.User

interface BaseRepository {

    fun saveUserInformationIntoUserDataClass(
        username: String,
        fullname: String,
        profile: String?,
        email: String,
        isAdmin: Boolean = false,
        isUser: Boolean = false,
    ):Boolean

    fun loadUserDataFromSharedPrefs(): Boolean

    fun saveUserInformationIntoSharedPrefs(
        username: String,
        fullname: String,
        email: String,
        profile: String?,
        isAdmin: Boolean = false,
        isUser: Boolean = false,
    ): Boolean

    suspend fun getUserInformationWithUsername(
        username: String,
    ): User?

    fun checkUserInformationFromSharedIsExist(): Boolean

    fun checkInformationFromSharedIsAdminOrUser(): Boolean

    fun encryptPassword(
        user: User,
    ): User

    fun decryptPassword(
        user: User,
    ): User

    fun getStringFromShared(
        key: String,
    ): String?

    fun getBooleanFromShared(
        key: String,
    ): Boolean

    fun clearUserData()

    fun clearSharedPrefs()
}