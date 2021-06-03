package com.abproject.tsp_cart.model.dataclass

object ApplicationData {
    var username: String? = null
        private set
    var email: String? = null
        private set
    var isAdmin: Boolean = false
        private set
    var isUser: Boolean = false
        private set
    var lastSeen: String? = null
        private set

    fun setUserOrAdmin(
        isUser: Boolean = false,
        isAdmin: Boolean = false,
    ) {
        this.isAdmin = isAdmin
        this.isUser = isUser
    }

    fun setUsername(
        username: String,
    ) {
        this.username = username
    }

    fun setEmail(
        email: String,
    ) {
        this.email = email
    }

    fun clearApplicationData() {
        this.username = null
        this.isAdmin = false
        this.isUser = false
    }
}