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
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.myapplication2.FragmentA
import com.example.myapplication2.FragmentB
import com.google.android.material.textfield.TextInputLayout

class SignInActivity : AppCompatActivity() {
    private var isFragmentA = true // Tracks which fragment is currently displayed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Add FragmentA initially
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, FragmentA())
            }
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

        // Fragment toggle button logic
        val toggleFragmentButton = findViewById<Button>(R.id.switchFragmentButton)
        toggleFragmentButton.setOnClickListener {
            toggleFragment()
        }
    }

    private fun toggleFragment() {
        val fragment: Fragment = if (isFragmentA) {
            FragmentB()
        } else {
            FragmentA()
        }
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
        }
        isFragmentA = !isFragmentA // Toggle the flag
    }
}