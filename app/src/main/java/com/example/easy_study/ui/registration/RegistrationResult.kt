package com.example.easy_study.ui.registration

import com.example.easy_study.data.model.LoggedInUser

data class RegistrationResult(
    val success: LoggedInUser? = null,
    val error: Int? = null
)