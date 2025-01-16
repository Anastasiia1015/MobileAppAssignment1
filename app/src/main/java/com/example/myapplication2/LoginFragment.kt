package com.example.myapplication2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

class LoginFragment(private val credentialsManager: CredentialsManager) : Fragment() {

    private var listener: EventsInterface? = null

    interface EventsInterface {
        fun onLoginPressed()
        fun onSwitchToRegisterPressed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is EventsInterface) {
            throw IllegalArgumentException("Activity must implement EventsInterface")
        }
        listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        val loginButton = view.findViewById<View>(R.id.loginButton)
        val registerSwitchButton = view.findViewById<View>(R.id.registerSwitchButton)

        loginButton.setOnClickListener { handleLogin(view) }
        registerSwitchButton.setOnClickListener { listener?.onSwitchToRegisterPressed() }
    }

    private fun handleLogin(view: View) {
        val emailInput = view.findViewById<TextInputLayout>(R.id.emailTextInputLayout)
        val passwordInput = view.findViewById<TextInputLayout>(R.id.passwordTextInputLayout)

        val emailText = emailInput.editText?.text.toString()
        val passwordText = passwordInput.editText?.text.toString()

        if (isValidLogin(emailText, passwordText, emailInput)) {
            listener?.onLoginPressed()
        }
    }

    private fun isValidLogin(
        email: String,
        password: String,
        emailInput: TextInputLayout
    ): Boolean {
        return if (credentialsManager.login(email, password)) {
            clearErrors(emailInput)
            true
        } else {
            emailInput.error = getString(R.string.invalid_credentials_error)
            false
        }
    }

    private fun clearErrors(vararg inputLayouts: TextInputLayout) {
        inputLayouts.forEach { it.error = null }
    }
}
