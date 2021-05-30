package com.abproject.tsp_cart.model.dataclass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "user_name")
    var username: String,
    var password: String,
    @ColumnInfo(name = "full_name")
    var fullname: String,
    var email: String,
    var profile: String? = null,
    @ColumnInfo(name = "is_manager")
    var isManager: Boolean = false,
    @ColumnInfo(name = "created_at")
    var createdAt: String,
    @ColumnInfo(name = "last_seen")
    var lastSeen: String = "",
)
