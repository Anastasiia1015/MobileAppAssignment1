// Put your package name here. Check your activity for reference.
package com.example.myapplication2

import org.junit.Test
import org.junit.Assert.*

class CredentialsManagerTest {

    // Test empty email
    @Test
    fun givenEmptyEmail_thenReturnFalse() {
        val credentialsManager = CredentialsManager()
        assertEquals(
            false, credentialsManager.isEmailValid("")
        )
    }

    // Test wrong email format
    @Test
    fun givenWrongEmailFormat_thenReturnFalse() {
        val credentialsManager = CredentialsManager()
        assertEquals(
            false, credentialsManager.isEmailValid("mynamegmail.com")
        )
        assertEquals(
            false, credentialsManager.isEmailValid("mynamegmail.@com")
        )
        assertEquals(
            false, credentialsManager.isEmailValid("mynamegmail.com@")
        )
    }

    // Test proper email
    @Test
    fun givenCorrectEmailFormat_thenReturnTrue() {
        val credentialsManager = CredentialsManager()
        assertEquals(
            true, credentialsManager.isEmailValid("myname@gmail.com")
        )

    }

    // Test empty password
    @Test
    fun givenEmptyPassword_thenReturnFalse() {
        val credentialsManager = CredentialsManager()
        assertEquals(
            false, credentialsManager.isValidPassword("")
        )
    }

    // Test filled password
    @Test
    fun givenNotEmptyPassword_thenReturnTrue() {
        val credentialsManager = CredentialsManager()
        assertEquals(
            true, credentialsManager.isValidPassword("12345678")
        )
    }

    @Test
    fun givenRightData() {
        val credentialsManager = CredentialsManager()
        assertEquals(true, credentialsManager.login("test@et.st", "1234"))
    }

    @Test
    fun givenWrongData() {
        val credentialsManager = CredentialsManager()
        assertEquals(false, credentialsManager.login("something", "something"))
    }
}