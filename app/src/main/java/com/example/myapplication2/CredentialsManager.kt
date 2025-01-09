package com.example.myapplication2

class CredentialsManager {
    private val userAccounts = mutableMapOf<String, String>(
        "test@te.st" to "1234"
    )

    fun checkIfEmailExists(email: String): Boolean {
        val normalizedEmail = email.lowercase().trim()
        return userAccounts.containsKey(normalizedEmail)
    }

    fun isEmailValid(email: String): Boolean {
        if (email.isEmpty()) return false
        val emailRegex = Regex(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        )
        return email.matches(emailRegex)
    }

    fun isValidPassword(password: String): Boolean {
        return password.isNotEmpty()
    }

    fun login(email: String, password: String): Boolean {
        val normalizedEmail = email.lowercase().trim()
        return userAccounts[normalizedEmail] == password
    }

    fun register(fullName: String, email: String, phoneNumber: String, password: String): Boolean {
        if (isEmailValid(email) && isValidPassword(password)) {
            userAccounts[email.lowercase()] = password
            return true
        }
        return false
    }
}
