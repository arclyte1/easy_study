package com.example.easy_study.data.network

data class RegistrationRequest(
    val email: String,
    val name: String,
    val role: String,
    val password: String,
)