package com.abproject.tsp_cart.model.repository

import com.abproject.tsp_cart.model.dataclass.User

interface AuthRepository {

    suspend fun insertUser(user: User): Boolean
    suspend fun searchInUsersByUsername(query: String): User?
}