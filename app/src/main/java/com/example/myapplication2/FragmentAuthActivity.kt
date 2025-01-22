package com.example.myapplication2

import android.content.Intent

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit

class FragmentAuthActivity :
    AppCompatActivity(R.layout.activity_fragment_auth), LoginFragment.EventsInterface,
    RegisterFragment.EventsInterface {
    private val credentialsManager = CredentialsManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fragment_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportFragmentManager.commit {
            val fragment = RegisterFragment(credentialsManager)
            replace(R.id.fragmentContainerView, fragment)
        }
    }
    override fun onLoginPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    override fun onRegisterPressed() {
        supportFragmentManager.commit {
            val fragment = LoginFragment(credentialsManager)
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack(null)
        }
    }
    override fun onSwitchToLoginPressed() {
        supportFragmentManager.commit {
            val fragment = LoginFragment(credentialsManager)
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack(null)
        }
    }
    override fun onSwitchToRegisterPressed() {
        supportFragmentManager.commit {
            val fragment = RegisterFragment(credentialsManager)
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack(null)
        }
    }
}