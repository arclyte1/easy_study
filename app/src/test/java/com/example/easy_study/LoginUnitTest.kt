package com.example.easy_study

import com.example.easy_study.common.Constants
import com.example.easy_study.common.Resource
import com.example.easy_study.data.remote.EasyStudyApi
import com.example.easy_study.data.repository.LoginRepositoryImpl
import com.example.easy_study.domain.use_case.LoginUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginUnitTest {

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        val api = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EasyStudyApi::class.java)
        val repository = LoginRepositoryImpl(api)
        loginUseCase = LoginUseCase(repository)
    }

    @Test
    fun `Test login existing user with correct password`() = runBlocking {
        loginUseCase(
            email = "admin@admin.com",
            password = "admin"
        ).collect {
            assert(it !is Resource.Error)
            if (it is Resource.Success) {
                assert(it.data!!.email == "admin@admin.com")
            }
        }
    }

    @Test
    fun `Test login existing user with incorrect password`() = runBlocking {
        loginUseCase(
            email = "admin@admin.com",
            password = "123"
        ).collect {
            assert(it is Resource.Error || it is Resource.Loading)
        }
    }

    @Test
    fun `Test login not existing user`() = runBlocking {
        loginUseCase(
            email = "somerandomtext",
            password = "123"
        ).collect {
            assert(it is Resource.Error || it is Resource.Loading)
        }
    }
}