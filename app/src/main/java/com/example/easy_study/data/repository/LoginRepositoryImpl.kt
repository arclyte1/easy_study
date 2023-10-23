package com.example.easy_study.data.repository

import com.example.easy_study.data.remote.EasyStudyApi
import com.example.easy_study.data.remote.request.LoginRequest
import com.example.easy_study.data.remote.request.RegistrationRequest
import com.example.easy_study.domain.model.LoggedInUser
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.domain.repository.LoginRepository
import javax.inject.Inject


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepositoryImpl @Inject constructor (
    private val api: EasyStudyApi
): LoginRepository {

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
    }

    override suspend fun login(email: String, password: String): LoggedInUser {
        val request = LoginRequest(email, password)
        val loginResponse = api.login(request)
        val user = LoggedInUser(
            userId = loginResponse.userId,
            email = loginResponse.email,
            refresh = loginResponse.refresh,
            access = loginResponse.access,
            name = loginResponse.name,
            role = UserRole.getValue(loginResponse.role),
            studying_groups = loginResponse.studying_groups,
            teaching_groups = loginResponse.teaching_groups,
        )
        setLoggedInUser(user)
        return user
    }

    override suspend fun register(email: String, username: String, role: UserRole, password: String): LoggedInUser {
        val request = RegistrationRequest(email, username, UserRole.toString(role), password)
        val registrationResponse = api.register(request)
        val user =  LoggedInUser(
            userId = registrationResponse.userId,
            email = registrationResponse.email,
            refresh = registrationResponse.refresh,
            access = registrationResponse.access,
            name = registrationResponse.name,
            role = UserRole.getValue(registrationResponse.role),
            studying_groups = registrationResponse.studying_groups,
            teaching_groups = registrationResponse.teaching_groups,
        )
        setLoggedInUser(user)
        return user
    }

    override fun getLoggedInUser(): LoggedInUser? {
        return user
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}