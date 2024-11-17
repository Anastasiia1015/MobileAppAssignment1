package com.example.myapplication2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.example.myapplication2.CredentialsManager

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val registerNowLabel = findViewById<View>(R.id.register)
        registerNowLabel.setOnClickListener {
            val intent = Intent(this, CreateAccActivity::class.java)
            startActivity(intent)
        }

        val nextButton = findViewById<Button>(R.id.nextButton)
        val credentialsManager = CredentialsManager()
        val emailInput = findViewById<TextInputLayout>(R.id.emailInput)
        val passwordInput = findViewById<TextInputLayout>(R.id.passwordInput)
        nextButton.setOnClickListener {
            val emailText = emailInput.editText?.text.toString()
            val passwordText = passwordInput.editText?.text.toString()
            Log.d("Auth", "clicked button $emailText $passwordText")
            if (credentialsManager.login(emailText, passwordText)) {
                Log.d("Auth", "password ok")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                emailInput.error = getString(R.string.error_invalid_data)
            }
        }
    }
}