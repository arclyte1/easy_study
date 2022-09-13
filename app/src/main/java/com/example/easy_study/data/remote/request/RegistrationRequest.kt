package com.example.easy_study.data.remote.request

data class RegistrationRequest(
    val email: String,
    val name: String,
    val role: String,
    val password: String,
)