package com.library.model

data class LibraryUser(val name: String, val email: String) {
    init {
        require(isValidEmail(email)) { "Невалидный email: $email" }
    }
    companion object {
        private val EMAIL_REGEX = Regex(
            """^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""" )
        fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email
        )
    }
}
