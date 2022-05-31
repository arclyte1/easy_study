package com.example.easy_study.data.network.request

data class RegistrationRequest(
    val email: String,
    val name: String,
    val role: String,
    val password: String,
)