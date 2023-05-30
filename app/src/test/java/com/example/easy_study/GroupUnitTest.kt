package com.example.easy_study

import com.example.easy_study.common.Constants
import com.example.easy_study.common.Resource
import com.example.easy_study.data.remote.EasyStudyApi
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.data.repository.LoginRepositoryImpl
import com.example.easy_study.domain.use_case.AddGroupUseCase
import com.example.easy_study.domain.use_case.GetGroupDetailsUseCase
import com.example.easy_study.domain.use_case.GetGroupsUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class GroupUnitTest {

    private lateinit var addGroupUseCase: AddGroupUseCase
    private lateinit var getGroupsUseCase: GetGroupsUseCase
    private lateinit var getGroupDetailsUseCase: GetGroupDetailsUseCase

    @Before
    fun setUp() = runBlocking {
        val api = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EasyStudyApi::class.java)
        val loginRepository = LoginRepositoryImpl(api)
        val groupRepository = GroupRepositoryImpl(api, loginRepository)
        loginRepository.login("admin@admin.com", "admin")
        addGroupUseCase = AddGroupUseCase(groupRepository)
        getGroupsUseCase = GetGroupsUseCase(groupRepository)
        getGroupDetailsUseCase = GetGroupDetailsUseCase(groupRepository)
    }

    @Test
    fun `Test create group`() = runBlocking {
        val title = "test${Random.nextLong()}"
        addGroupUseCase(
            title = title,
            subject = "test"
        ).collect { resource ->
            assert(resource !is Resource.Error)
            if (resource is Resource.Success) {
                assert(resource.data!!.find { it.group_title == title } != null)
            }
        }
    }

    @Test
    fun `Test get groups list`() = runBlocking {
        getGroupsUseCase().collect { resource ->
            assert(resource !is Resource.Error)
            if (resource is Resource.Success) {
                assert(resource.data!!.find { it.group_title == "test" } != null)
            }
        }
    }

    @Test
    fun `Test get group details`() = runBlocking {
        getGroupDetailsUseCase(4L).collect { resource ->
            assert(resource !is Resource.Error)
            if (resource is Resource.Success) {
                assert(resource.data!!.teachers.find { it.email == "admin@admin.com" } != null)
            }
        }
    }

    @Test
    fun `Test get not existing group details`() = runBlocking {
        getGroupDetailsUseCase(-1L).collect { resource ->
            assert(resource is Resource.Error || resource is Resource.Loading)
        }
    }
}