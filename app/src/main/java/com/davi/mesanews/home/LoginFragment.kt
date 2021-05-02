package com.davi.mesanews.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.davi.mesanews.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passwordLayout = view.findViewById<TextInputLayout>(R.id.login_password)
        val emailEditText = view.findViewById<TextInputEditText>(R.id.login_email_field)
        val passwordEditText = view.findViewById<TextInputEditText>(R.id.login_password_field)
        val loginButton = view.findViewById<MaterialButton>(R.id.login_button)
        val loadingView : ConstraintLayout = view.findViewById(R.id.login_loading_view)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                passwordLayout.error = "Please fill all fields"
            } else {
                viewModel.performLogin(emailEditText.text.toString(), passwordEditText.text.toString())
                loadingView.visibility = View.VISIBLE
            }
        }

        viewModel.isSuccesfull.observe(viewLifecycleOwner, Observer {
            if (it) {
                Navigation.findNavController(view).navigate(R.id.newsActivity)
                loadingView.visibility = View.GONE
            } else {
                loadingView.visibility = View.GONE
                passwordLayout.error = "Wrong email or password"
            }
        })
    }
}