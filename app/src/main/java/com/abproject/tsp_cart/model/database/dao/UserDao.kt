package com.abproject.tsp_cart.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.abproject.tsp_cart.model.dataclass.User

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    /**
     * this function takes a username for searching in database.
     * if username has already exists @return User and if this
     * username didn't exist so @return null
     */
    @Query("SELECT * FROM tbl_user WHERE user_name == :usernameString")
    suspend fun searchInUsersByUsername(usernameString: String): User?
}