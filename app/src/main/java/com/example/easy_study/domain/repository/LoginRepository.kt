package com.example.easy_study.domain.repository

import com.example.easy_study.data.Result
import com.example.easy_study.domain.model.LoggedInUser
import com.example.easy_study.domain.model.UserRole

interface LoginRepository {

    suspend fun login(email: String, password: String): LoggedInUser

    suspend fun register(email: String,
                 username: String,
                 role: UserRole.Role,
                 password: String): LoggedInUser

    suspend fun getLoggedInUser(): LoggedInUser?
}