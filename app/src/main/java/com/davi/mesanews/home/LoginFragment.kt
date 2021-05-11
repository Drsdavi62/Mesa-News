package com.davi.mesanews.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.davi.mesanews.R
import com.davi.mesanews.utils.LoginInputEditText
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginFragment : Fragment(R.layout.login_fragment) {

    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText: LoginInputEditText = view.findViewById(R.id.login_email_field)

        val passwordEditText: LoginInputEditText = view.findViewById(R.id.login_password_field)

        val loginButton: MaterialButton = view.findViewById(R.id.login_button)

        val loadingView: ConstraintLayout = view.findViewById(R.id.login_loading_view)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            var shouldPerform = true

            if (email.isEmpty()) {
                emailEditText.setError(getString(R.string.login_empty_field_message))
                shouldPerform = false
            }

            if (password.isEmpty()) {
                passwordEditText.setError(getString(R.string.login_empty_field_message))
                shouldPerform = false
            }

            if (shouldPerform) {
                viewModel.performLogin(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                loadingView.visibility = View.VISIBLE
            }
        }

        viewModel.isSuccessful.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.newsActivity)
                loadingView.visibility = View.GONE
                requireActivity().finish()
            } else {
                loadingView.visibility = View.GONE
                emailEditText.setError(" ")
                passwordEditText.setError(getString(R.string.login_wrong_credentials_message))
            }
        })
    }
}