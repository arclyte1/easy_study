package com.example.easy_study.ui.login

import com.example.easy_study.data.model.LoggedInUser

data class LoginResult(
    val success: LoggedInUser? = null,
    val error: Int? = null
)