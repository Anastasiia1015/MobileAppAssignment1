package com.example.myapplication2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

class RegisterFragment(private val credentialsManager: CredentialsManager) : Fragment() {

    private var listener: EventsInterface? = null

    interface EventsInterface {
        fun onRegisterPressed()
        fun onSwitchToLoginPressed()
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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        val registerButton = view.findViewById<View>(R.id.registerButton)
        val loginSwitchButton = view.findViewById<View>(R.id.loginSwitchButton)

        registerButton.setOnClickListener { handleRegistration(view) }
        loginSwitchButton.setOnClickListener { listener?.onSwitchToLoginPressed() }
    }

    private fun handleRegistration(view: View) {
        val emailInput = view.findViewById<TextInputLayout>(R.id.validEmail)
        val passwordInput = view.findViewById<TextInputLayout>(R.id.strongPassword)
        val numberInput = view.findViewById<TextInputLayout>(R.id.phoneNum)
        val nameInput = view.findViewById<TextInputLayout>(R.id.fullName)

        val emailText = emailInput.editText?.text.toString()
        val passwordText = passwordInput.editText?.text.toString()
        val numberText = numberInput.editText?.text.toString()
        val nameText = nameInput.editText?.text.toString()

        when {
            isInvalidEmail(emailText, emailInput) -> return
            isInvalidPassword(passwordText, passwordInput) -> return
            else -> {
                credentialsManager.register(nameText, emailText, numberText, passwordText)
                listener?.onRegisterPressed()
            }
        }
    }

    private fun isInvalidEmail(email: String, inputLayout: TextInputLayout): Boolean {
        return if (credentialsManager.checkIfEmailExists(email) || !credentialsManager.isEmailValid(email)) {
            inputLayout.error = getString(R.string.email_exists_error)
            true
        } else {
            inputLayout.error = null
            false
        }
    }

    private fun isInvalidPassword(password: String, inputLayout: TextInputLayout): Boolean {
        return if (!credentialsManager.isValidPassword(password)) {
            inputLayout.error = getString(R.string.wrong_password_error)
            true
        } else {
            inputLayout.error = null
            false
        }
    }
}
