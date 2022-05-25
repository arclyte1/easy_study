package com.example.easy_study.data

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.example.easy_study.data.model.LoggedInUser
import com.example.easy_study.data.model.UserRole
import java.security.KeyStore
import kotlin.coroutines.coroutineContext


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(private val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(email: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(email, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    fun register(email: String, username: String, role: UserRole.Role, password: String): Result<LoggedInUser> {
        val result = dataSource.register(email, username, role, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) { // TODO save user credentials
        this.user = loggedInUser
    }

    companion object {
        val instance = LoginRepository(LoginDataSource())
    }
}