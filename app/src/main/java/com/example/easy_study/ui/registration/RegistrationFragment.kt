package com.example.easy_study.ui.registration

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.easy_study.R
import com.example.easy_study.data.model.LoggedInUser
import com.example.easy_study.data.model.UserRole
import com.example.easy_study.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        binding = FragmentRegistrationBinding.bind(view)

        val items: MutableList<String> = mutableListOf()
        for (i in UserRole.Role.values()) {
            items.add(resources.getString(UserRole.getRes(i)))
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.role_item, items)
        (binding.role.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        viewModel.registrationResult.observe(viewLifecycleOwner,
            Observer { registrationResult ->
                registrationResult ?: return@Observer
                registrationResult.error?.let {
                    showRegistrationFailed(it)
                }
                registrationResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        viewModel.registrationFormState.observe(viewLifecycleOwner,
            Observer { registrationFormState ->
                if (registrationFormState == null) {
                    return@Observer
                }
                binding.register.isEnabled = registrationFormState.isDataValid
                binding.email.error = if (registrationFormState.emailError == null) null else getString(registrationFormState.emailError)
                binding.username.error = if (registrationFormState.usernameError == null) null else getString(registrationFormState.usernameError)
                binding.password.error = if (registrationFormState.passwordError == null) null else getString(registrationFormState.passwordError)
                binding.role.error = if (registrationFormState.roleError == null) null else getString(registrationFormState.roleError)
            })

        viewModel.signingUp.observe(viewLifecycleOwner,
            Observer { signingUp ->
                signingUp ?: return@Observer
                if (signingUp)
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
                viewModel.registrationDataChanged(
                    binding.email.editText?.text.toString(),
                    binding.username.editText?.text.toString(),
                    binding.role.editText?.text.toString(),
                    binding.password.editText?.text.toString()
                )
            }
        }

        binding.username.editText?.addTextChangedListener(afterTextChangedListener)
        binding.email.editText?.addTextChangedListener(afterTextChangedListener)
        binding.password.editText?.addTextChangedListener(afterTextChangedListener)
        binding.role.editText?.addTextChangedListener(afterTextChangedListener)


        binding.register.setOnClickListener {
            viewModel.register(
                email = binding.email.editText?.text.toString(),
                username = binding.username.editText?.text.toString(),
                role = UserRole.getValue(requireContext(), binding.role.editText!!.text.toString()),
                password = binding.password.editText?.text.toString()
            )
        }
    }

    private fun updateUiWithUser(user: LoggedInUser) {
        val bundle = bundleOf("role" to UserRole.toString(user.role))
        findNavController().navigate(R.id.action_registrationFragment_to_studentGroupFragment, bundle)
    }

    private fun showRegistrationFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

}