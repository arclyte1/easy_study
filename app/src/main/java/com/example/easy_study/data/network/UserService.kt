package com.example.easy_study.data.network

import com.example.easy_study.data.model.Group
import com.example.easy_study.data.model.Lesson
import com.example.easy_study.data.network.request.*
import com.example.easy_study.data.network.response.LoginResponse
import com.example.easy_study.data.network.response.RegistrationResponse
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @POST("login/")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("registration/")
    fun register(@Body registrationRequest: RegistrationRequest): Call<RegistrationResponse>

    @GET("groups/")
    fun groups(@Header("Authorization") token: String): Call<List<Group>>

    @GET("groups/{groupId}/lessons/")
    fun studentLessons(@Header("Authorization") token: String,
                       @Path("groupId") groupId: Long): Call<List<Lesson>>

    @POST("lessons/{lessonId}/marks/")
    fun setMark(@Header("Authorization") token: String,
                @Path("lessonId") lessonId: Long,
                @Body request: SetMarkRequest
    ) : Call<Unit>

    @DELETE("lessons/{lessonId}/marks/")
    fun deleteMark(@Header("Authorization") token: String,
                   @Path("lessonId") lessonId: Long,
                   @Body request: DeleteMarkRequest
    ) : Call<Unit>

    @POST("lessons/{lessonId}/attendances/")
    fun setAttendance(@Header("Authorization") token: String,
                      @Path("lessonId") lessonId: Long,
                      @Body request: SetAttendanceRequest
    ) : Call<Unit>

    @POST("groups/")
    fun createGroup(@Header("Authorization") token: String,
                    @Body request: CreateGroupRequest
    ) : Call<Group>

    @POST("groups/{groupId}/lessons/")
    fun createLesson(@Header("Authorization") token: String,
                     @Path("groupId") groupId: Long,
                     @Body request: CreateLessonRequest
    ) : Call<Lesson>

    @POST("groups/{groupId}/students/")
    fun addStudent(@Header("Authorization") token: String,
                     @Path("groupId") groupId: Long,
                     @Body request: AddStudentRequest
    ) : Call<Group>
}