package com.abproject.tsp_cart.model.dataclass

object UserData {
    var username: String? = null
        private set
    var fullName: String? = null
        private set
    var profile: String? = null
        private set
    var email: String? = null
        private set
    var isAdmin: Boolean = false
        private set
    var isUser: Boolean = false
        private set

    fun setupUserData(
        username: String,
        fullName: String,
        profile: String?,
        email: String,
        isAdmin: Boolean = false,
        isUser: Boolean = false,
    ) {
        this.username = username
        this.fullName = fullName
        this.profile = profile
        this.email = email
        this.isAdmin = isAdmin
        this.isUser = isUser
    }

    fun clearUserData() {
        username = null
        fullName = null
        profile = null
        email = null
        isAdmin = false
        isUser = false
    }
}