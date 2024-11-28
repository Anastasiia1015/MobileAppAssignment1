package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout

class CreateAccActivity : AppCompatActivity() {
    private val credentialsManager = CredentialsManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_acc)

        val emailInput = findViewById<TextInputLayout>(R.id.emailInput)
        val passwordInput = findViewById<TextInputLayout>(R.id.passwordInput)
        val registerButton = findViewById<Button>(R.id.nextButton)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val loginLabel = findViewById<View>(R.id.login)
        loginLabel.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val email = emailInput.editText?.text.toString()
            val password = passwordInput.editText?.text.toString()

            if (!credentialsManager.isEmailValid(email)) {
                emailInput.error = "Invalid email"
                return@setOnClickListener
            }
            if (!credentialsManager.isValidPassword(password)) {
                passwordInput.error = "Invalid password"
                return@setOnClickListener
            }

            if (credentialsManager.register(email, password)) {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            } else {
                emailInput.error = "Email taken"
            }
        }
    }
}