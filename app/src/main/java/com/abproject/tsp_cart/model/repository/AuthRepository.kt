package com.abproject.tsp_cart.model.repository

import com.abproject.tsp_cart.model.dataclass.User

interface AuthRepository {

    suspend fun insertUser(
        user: User,
    ): Boolean

    suspend fun searchInUsersByUsername(
        query: String,
    ): User?

    fun loadApplicationData(
        username: String? = null,
        email: String? = null,
        isAdmin: Boolean = false,
        isUser: Boolean = false,
    )

    fun loadApplicationDataFromSharedPrefs()

    fun saveApplicationDataInSharedPrefs(
        username: String? = null,
        email: String? = null,
        isAdmin: Boolean = false,
        isUser: Boolean = false,
    )


    fun checkDataFromShared(): Boolean
    fun checkAdminPanelFromShared(): Boolean
}