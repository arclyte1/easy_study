package com.example.easy_study.presentation.screen.registration

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easy_study.R
import com.example.easy_study.common.Resource
import com.example.easy_study.common.ValidatingResult
import com.example.easy_study.data.Result
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.domain.use_case.RegistrationUseCase
import com.example.easy_study.presentation.navigation.AppNavigator
import com.example.easy_study.presentation.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor (
    private val appNavigator: AppNavigator,
    private val registrationUseCase: RegistrationUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow(RegistrationState())
    val screenState: StateFlow<RegistrationState> = _screenState

    fun register(email: String, username: String, role: UserRole, password: String) {
        registrationUseCase(email, username, role, password).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _screenState.update {
                        it.copy(
                            isSigningUp = false
                        )
                    }
                    navigateToGroupDetails()
                }
                is Resource.Error -> {
                    _screenState.update {
                        it.copy(
                            isSigningUp = false
                        )
                    }
                }
                is Resource.Loading -> {
                    _screenState.update {
                        it.copy(
                            isSigningUp = true
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun validateUsername(username: String): ValidatingResult {
        return when {
            username.length !in 6..20 -> ValidatingResult.Invalid(R.string.invalid_username_length)
            username.contains("[^a-zA-Z0-9_]+") -> ValidatingResult.Invalid(R.string.invalid_username_content)
            else -> ValidatingResult.Valid
        }
    }

    fun validateEmail(email: String): ValidatingResult {
        return if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            ValidatingResult.Valid
        else
            ValidatingResult.Invalid(R.string.invalid_email)
    }

    fun validatePassword(password: String): ValidatingResult {
        return if (password.length !in 6..20)
            ValidatingResult.Invalid(R.string.invalid_password)
        else
            ValidatingResult.Valid
    }

    fun navigateBack() {
        appNavigator.tryNavigateBack()
    }
    private fun navigateToGroupDetails() {
        appNavigator.tryNavigateTo(
            route = Destination.GroupListScreen(),
            popUpToRoute = Destination.LoginScreen(),
            inclusive = true
        )
    }
}