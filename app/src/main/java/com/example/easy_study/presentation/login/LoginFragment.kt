package com.example.easy_study.presentation.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.example.easy_study.R
import com.example.easy_study.common.Constants
import com.example.easy_study.domain.model.LoggedInUser
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.login.isEnabled = true

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                binding.login.isEnabled = loginFormState.isDataValid
                binding.email.error = if (loginFormState.emailError == null) null else getString(loginFormState.emailError)
                binding.password.error = if (loginFormState.passwordError == null) null else getString(loginFormState.passwordError)
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        loginViewModel.loggingIn.observe(viewLifecycleOwner,
            Observer { loggingIn ->
                loggingIn ?: return@Observer
                if (loggingIn)
                    binding.loading.visibility = View.VISIBLE
                else
                    binding.loading.visibility = View.GONE
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    binding.email.editText?.text.toString(),
                    binding.password.editText?.text.toString()
                )
            }
        }
        // TODO: textChangedListener
//        binding.email.editText?.addTextChangedListener(afterTextChangedListener)
//        binding.password.editText?.addTextChangedListener(afterTextChangedListener)
        binding.password.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    binding.email.editText?.text.toString(),
                    binding.password.editText?.text.toString()
                )
            }
            false
        }

        binding.login.setOnClickListener {
            loginViewModel.login(
                binding.email.editText?.text.toString(),
                binding.password.editText?.text.toString()
            )
        }

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

    }

    private fun updateUiWithUser(user: LoggedInUser) {
        val bundle = bundleOf(
            Constants.ROLE_FIELD to UserRole.toString(user.role)
        )
        findNavController().navigate(R.id.action_loginFragment_to_groupListFragment, bundle)
    }

    private fun showLoginFailed(errorString: String) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}