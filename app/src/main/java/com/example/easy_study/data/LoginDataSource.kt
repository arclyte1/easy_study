package com.example.easy_study.data

import com.example.easy_study.data.model.LoggedInUser
import com.example.easy_study.data.model.UserRole
import com.example.easy_study.data.network.LoginRequest
import com.example.easy_study.data.network.RegistrationRequest
import com.example.easy_study.data.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(email: String, password: String): Result<LoggedInUser> {
        val request = LoginRequest(email, password)
        val loginResponse = RetrofitClient.apiInterface.login(request).execute()
        if (loginResponse.isSuccessful) {
            val user = LoggedInUser(
                userId = loginResponse.body()!!.userId,
                email = loginResponse.body()!!.email,
                refresh = loginResponse.body()!!.refresh,
                access = loginResponse.body()!!.access,
                name = loginResponse.body()!!.name,
                role = UserRole.getValue(loginResponse.body()!!.role),
                studying_groups = loginResponse.body()!!.studying_groups,
                teaching_groups = loginResponse.body()!!.teaching_groups,
            )
            return Result.Success(user)
        }
        return Result.Error(IOException("Error logging in"))
    }

    fun register(email: String, username: String, role: UserRole.Role, password: String): Result<LoggedInUser> {
        val request = RegistrationRequest(email, username, UserRole.toString(role), password)
        val registrationResponse = RetrofitClient.apiInterface.register(request).execute()
        return if (registrationResponse.isSuccessful) {
            val user =  LoggedInUser(
                userId = registrationResponse.body()!!.userId,
                email = registrationResponse.body()!!.email,
                refresh = registrationResponse.body()!!.refresh,
                access = registrationResponse.body()!!.access,
                name = registrationResponse.body()!!.name,
                role = UserRole.getValue(registrationResponse.body()!!.role),
                studying_groups = registrationResponse.body()!!.studying_groups,
                teaching_groups = registrationResponse.body()!!.teaching_groups,
            )
            Result.Success(user)
        } else Result.Error(IOException("Registration error"))
    }

    fun logout() {
        // TODO: revoke authentication
    }
}