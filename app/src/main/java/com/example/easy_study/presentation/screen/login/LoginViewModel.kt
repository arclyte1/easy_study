package com.example.easy_study.presentation.screen.login

import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.easy_study.common.Resource
import com.example.easy_study.domain.use_case.LoginUseCase
import com.example.easy_study.presentation.navigation.AppNavigator
import com.example.easy_study.presentation.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor (
    private val appNavigator: AppNavigator,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        loginUseCase(email, password).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _loginState.update {
                        it.copy(
                            isLoggingIn = false
                        )
                    }
                    navigateToGroupDetails()
                }
                is Resource.Error -> {
                    _loginState.update {
                        it.copy(
                            isLoggingIn = false,
                            errorMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> {
                    _loginState.update {
                        it.copy(
                            isLoggingIn = true
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToRegistration() {
        appNavigator.tryNavigateTo(Destination.RegistrationScreen())
    }

    fun isEmailValid(email: String): Boolean {
        return if (email.isBlank()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length in 6..20
    }

    private fun navigateToGroupDetails() {
        appNavigator.tryNavigateTo(
            route = Destination.GroupListScreen(),
            popUpToRoute = Destination.LoginScreen(),
            inclusive = true
        )
    }
}