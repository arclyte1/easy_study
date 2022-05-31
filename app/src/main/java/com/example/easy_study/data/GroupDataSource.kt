package com.example.easy_study.data

import com.example.easy_study.data.model.Group
import com.example.easy_study.data.model.LoggedInUser
import com.example.easy_study.data.model.Lesson
import com.example.easy_study.data.network.*
import com.example.easy_study.data.network.request.*
import java.io.IOException

class GroupDataSource {

    fun getGroups(user: LoggedInUser): Result<List<Group>> {
        val response = RetrofitClient.apiInterface.groups("Bearer ${user.access}").execute()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else Result.Error(IOException("Groups error"))
    }

    fun getStudentGroupDetails(user: LoggedInUser, groupId: Long): Result<List<Lesson>> {
        val response = RetrofitClient.apiInterface.studentLessons("Bearer ${user.access}", groupId).execute()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else Result.Error(IOException("Group details error"))
    }

    fun setMark(user: LoggedInUser, lessonId: Long, studentId: Long, mark: Double?): Result<Unit> {
        val request = SetMarkRequest(studentId, mark)
        val response = RetrofitClient.apiInterface.setMark("Bearer ${user.access}", lessonId, request).execute()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else Result.Error(IOException("Set mark error"))
    }

    fun deleteMark(user: LoggedInUser, lessonId: Long, studentId: Long): Result<Unit> {
        val request = DeleteMarkRequest(studentId)
        val response = RetrofitClient.apiInterface.deleteMark("Bearer ${user.access}", lessonId, request).execute()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else Result.Error(IOException("Delete mark error"))
    }

    fun setAttendance(user: LoggedInUser, lessonId: Long, studentId: Long, attendance: Boolean): Result<Unit> {
        val request = SetAttendanceRequest(studentId, attendance)
        val response = RetrofitClient.apiInterface.setAttendance("Bearer ${user.access}", lessonId, request).execute()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else Result.Error(IOException("Set attendance error"))
    }

    fun createGroup(user: LoggedInUser, title: String, subject: String): Result<Group> {
        val request = CreateGroupRequest(title, subject)
        val response = RetrofitClient.apiInterface.createGroup("Bearer ${user.access}", request).execute()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else Result.Error(IOException("Create group error"))
    }

    fun createLesson(user: LoggedInUser, title: String, groupId: Long): Result<Lesson> {
        val request = CreateLessonRequest(title)
        val response = RetrofitClient.apiInterface.createLesson("Bearer ${user.access}", groupId, request).execute()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else Result.Error(IOException("Create lesson error"))
    }

    fun addStudent(user: LoggedInUser, email: String, groupId: Long): Result<Group> {
        val request = AddStudentRequest(email)
        val response = RetrofitClient.apiInterface.addStudent("Bearer ${user.access}", groupId, request).execute()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else Result.Error(IOException("Create lesson error"))
    }

}