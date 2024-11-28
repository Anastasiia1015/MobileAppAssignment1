// Put your package name here. Check your activity for reference.
package com.example.myapplication2

class CredentialsManager {
    private val userAccounts = mutableMapOf<String, String>(
        Pair<String, String>("test@te.st", "1234")
    )

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
        return userAccounts[email] == password
    }

    fun register(email: String, password: String): Boolean {
        if (isEmailValid(email) && isValidPassword((password))) {
            if (userAccounts.containsKey(email)) {
                    return false
            } else {
                userAccounts[email] = password
                return true
            }
        } else {
            return false
        }

    }
}
