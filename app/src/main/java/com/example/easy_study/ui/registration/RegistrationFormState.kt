package com.example.easy_study.ui.registration

class RegistrationFormState(
    val emailError: Int? = null,
    val usernameError: Int? = null,
    val roleError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)