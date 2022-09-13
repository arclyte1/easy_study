package com.example.easy_study.presentation.registration

import com.example.easy_study.domain.model.LoggedInUser

data class RegistrationResult(
    val success: LoggedInUser? = null,
    val error: String? = null
)