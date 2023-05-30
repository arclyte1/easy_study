package com.example.easy_study.data.remote

import com.example.easy_study.domain.model.Group
import com.example.easy_study.domain.model.Lesson
import com.example.easy_study.data.remote.request.*
import com.example.easy_study.data.remote.response.LoginResponse
import com.example.easy_study.data.remote.response.RegistrationResponse
import com.example.easy_study.domain.model.Student
import retrofit2.Call
import retrofit2.http.*

interface EasyStudyApi {
    @POST("login/")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("registration/")
    suspend fun register(
        @Body registrationRequest: RegistrationRequest
    ): RegistrationResponse

    @GET("groups/")
    suspend fun getGroups(
        @Header("Authorization") token: String
    ): List<Group>

    @GET("groups/{groupId}/lessons/")
    suspend fun getLessonList(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: Long
    ): List<Lesson>

    @GET("lessons/{lessonId}/students/")
    suspend fun getStudentList(
        @Header("Authorization") token: String,
        @Path("lessonId") lessonId: Long
    ): List<Student>

    @GET("groups/{groupId}/student_progress")
    suspend fun getStudentProgress(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: Long,
        @Query("email") email: String
    ): List<Lesson>

    @POST("lessons/{lessonId}/marks/")
    suspend fun setMark(
        @Header("Authorization") token: String,
        @Path("lessonId") lessonId: Long,
        @Body request: SetMarkRequest
    )

    @DELETE("lessons/{lessonId}/marks/")
    suspend fun deleteMark(
        @Header("Authorization") token: String,
        @Path("lessonId") lessonId: Long,
        @Body request: DeleteMarkRequest
    )

    @POST("lessons/{lessonId}/attendances/")
    suspend fun setAttendance(
        @Header("Authorization") token: String,
        @Path("lessonId") lessonId: Long,
        @Body request: SetAttendanceRequest
    )

    @POST("groups/")
    suspend fun createGroup(
        @Header("Authorization") token: String,
        @Body request: CreateGroupRequest
    ) : Group

    @POST("groups/{groupId}/lessons/")
    suspend fun createLesson(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: Long,
        @Body request: CreateLessonRequest
    ) : Lesson

    @POST("groups/{groupId}/students/")
    suspend fun addStudent(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: Long,
        @Body request: AddStudentRequest
    ) : Group

    @POST("groups/{groupId}/teachers/")
    suspend fun addTeacher(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: Long,
        @Body request: AddTeacherRequest
    ) : Group
}