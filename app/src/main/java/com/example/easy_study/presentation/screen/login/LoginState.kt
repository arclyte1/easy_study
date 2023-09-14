package com.example.easy_study.presentation.screen.login

data class LoginState(
    val isLoggingIn: Boolean = false,
    val errorMessage: String? = null,
)