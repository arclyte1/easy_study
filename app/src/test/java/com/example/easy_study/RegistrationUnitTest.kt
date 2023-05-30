package com.example.easy_study

import com.example.easy_study.common.Constants
import com.example.easy_study.common.Resource
import com.example.easy_study.data.remote.EasyStudyApi
import com.example.easy_study.data.repository.LoginRepositoryImpl
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.domain.use_case.RegistrationUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random


class RegistrationUnitTest {

    private lateinit var registrationUseCase: RegistrationUseCase

    @Before
    fun setUp() {
        val api = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EasyStudyApi::class.java)
        val repository = LoginRepositoryImpl(api)
        registrationUseCase = RegistrationUseCase(repository)
    }

    @Test
    fun `Test registration user with valid data`() = runBlocking {
        val email = "test${Random.nextLong()}@example.com"
        registrationUseCase(
            username = "test",
            email = email,
            password = "test_pass",
            role = UserRole.Role.STUDENT
        ).collect {
            assert(it !is Resource.Error)
            if (it is Resource.Success) {
                assert(it.data!!.email == email)
            }
        }
    }

    @Test
    fun `Test registration user with not unique email`() = runBlocking {
        val email = "admin@admin.com"
        registrationUseCase(
            username = "test",
            email = email,
            password = "test_pass",
            role = UserRole.Role.STUDENT
        ).collect {
            assert(it is Resource.Loading || it is Resource.Error)
        }
    }
}