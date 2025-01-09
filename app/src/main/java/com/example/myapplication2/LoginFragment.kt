package com.example.myapplication2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
class LoginFragment(val credentialsManager: CredentialsManager) : Fragment() {
    private var listener: EventsInterface? = null
    interface EventsInterface {
        fun onLoginPressed()
        fun onSwitchToRegisterPressed()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        require(context is EventsInterface, {
            "Activity holding fragment must implement its EventsInterface"
        })
        listener = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.buttonNext).setOnClickListener {
            val emailInput = view.findViewById<TextInputLayout>(R.id.emailTextInputLayout)
            val passwordInput = view.findViewById<TextInputLayout>(R.id.passwordTextInputLayout)
            val emailText = emailInput.editText?.text.toString()
            val passwordText = passwordInput.editText?.text.toString()
            if (credentialsManager.login(emailText, passwordText)) {
                listener?.onLoginPressed()
            } else {
                emailInput.error = getString(R.string.email_exists_error)
            }
        }
        view.findViewById<View>(R.id.newMember).setOnClickListener {
            listener?.onSwitchToRegisterPressed()
        }
    }
}