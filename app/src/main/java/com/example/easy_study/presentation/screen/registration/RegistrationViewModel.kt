package com.example.easy_study.presentation.screen.registration

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easy_study.R
import com.example.easy_study.common.Resource
import com.example.easy_study.data.Result
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.domain.use_case.RegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor (
    private val registrationUseCase: RegistrationUseCase,
) : ViewModel() {

    private val _signingUp = MutableLiveData<Boolean>()
    val signingUp: LiveData<Boolean> = _signingUp

    fun register(email: String, username: String, role: UserRole, password: String) {
        registrationUseCase(email, username, role, password).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _signingUp.postValue(false)
//                    _registrationResult.postValue(RegistrationResult(success = result.data))
                }
                is Resource.Error -> {
                    _signingUp.postValue(false)
//                    _registrationResult.postValue(RegistrationResult(error = result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _signingUp.postValue(true)
                }
            }
        }.launchIn(viewModelScope)
    }

    // A placeholder email validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.isBlank()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length in 6..20
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.length in 6..20
    }

    // A placeholder role validation check
    private fun isRoleValid(role: String): Boolean {
        Log.d("role", role)
        return role.isNotEmpty()
    }
}