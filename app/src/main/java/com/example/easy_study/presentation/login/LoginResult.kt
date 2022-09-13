package com.example.easy_study.presentation.login

import com.example.easy_study.domain.model.LoggedInUser

data class LoginResult(
    val success: LoggedInUser? = null,
    val error: String? = null
)