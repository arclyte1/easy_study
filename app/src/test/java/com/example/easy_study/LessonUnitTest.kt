package com.example.easy_study

import com.example.easy_study.common.Constants
import com.example.easy_study.common.Resource
import com.example.easy_study.data.remote.EasyStudyApi
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.data.repository.LoginRepositoryImpl
import com.example.easy_study.domain.use_case.AddLessonUseCase
import com.example.easy_study.domain.use_case.GetLessonListUseCase
import com.example.easy_study.domain.use_case.GetStudentListUseCase
import com.example.easy_study.domain.use_case.GetStudentProgressUseCase
import com.example.easy_study.domain.use_case.SetAttendanceUseCase
import com.example.easy_study.domain.use_case.SetMarkUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class LessonUnitTest {

    private lateinit var addLessonUseCase: AddLessonUseCase
    private lateinit var getLessonListUseCase: GetLessonListUseCase
    private lateinit var getStudentListUseCase: GetStudentListUseCase
    private lateinit var setMarkUseCase: SetMarkUseCase
    private lateinit var setAttendanceUseCase: SetAttendanceUseCase
    private lateinit var getStudentProgressUseCase: GetStudentProgressUseCase

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
        addLessonUseCase = AddLessonUseCase(groupRepository)
        getLessonListUseCase = GetLessonListUseCase(groupRepository)
        getStudentListUseCase = GetStudentListUseCase(groupRepository)
        setMarkUseCase = SetMarkUseCase(groupRepository)
        setAttendanceUseCase = SetAttendanceUseCase(groupRepository)
        getStudentProgressUseCase = GetStudentProgressUseCase(groupRepository)
    }

    @Test
    fun `Test create lesson`() = runBlocking {
        val title = "test${Random.nextLong()}"
        addLessonUseCase(4, title).collect { resource ->
            assert(resource !is Resource.Error)
            if (resource is Resource.Success) {
                assert(resource.data!!.find { it.title == title } != null)
            }
        }
    }

    @Test
    fun `Test get lesson list`() = runBlocking {
        getLessonListUseCase(4).collect { resource ->
            assert(resource !is Resource.Error)
            if (resource is Resource.Success) {
                assert(resource.data!!.find { it.title == "testlesson" } != null)
            }
        }
    }

    @Test
    fun `Test get student list`() = runBlocking {
        getStudentListUseCase(1).collect { resource ->
            assert(resource !is Resource.Error)
            if (resource is Resource.Success) {
                assert(resource.data!!.find { it.email == "teststudent@example.com" } != null)
            }
        }
    }

    @Test
    fun `Test set mark`() = runBlocking {
        setMarkUseCase(1, 9, Random.nextDouble()).collect { resource ->
            assert(resource !is Resource.Error)
        }
    }

    @Test
    fun `Test set attendance`() = runBlocking {
        setAttendanceUseCase(1, 9, Random.nextBoolean()).collect { resource ->
            assert(resource !is Resource.Error)
        }
    }

    @Test
    fun `Test get student progress`() = runBlocking {
        getStudentProgressUseCase(4, "teststudent@example.com").collect { resource ->
            assert(resource !is Resource.Error)
        }
    }
}