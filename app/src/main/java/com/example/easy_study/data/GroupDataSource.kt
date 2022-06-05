package com.example.easy_study.data

import com.example.easy_study.data.model.Group
import com.example.easy_study.data.model.LoggedInUser
import com.example.easy_study.data.model.Lesson
import com.example.easy_study.data.network.*
import com.example.easy_study.data.network.request.*
import java.io.IOException
import java.net.ConnectException

class GroupDataSource {

    fun getGroups(user: LoggedInUser): Result<List<Group>> {
        return try {
            val response = RetrofitClient.apiInterface.groups("Bearer ${user.access}").execute()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else Result.Error(IOException("Groups error"))
        } catch (e: ConnectException) {
            Result.Error(e)
        }
    }

    fun getStudentGroupDetails(user: LoggedInUser, groupId: Long): Result<List<Lesson>> {
        return try {
            val response = RetrofitClient.apiInterface.studentLessons("Bearer ${user.access}", groupId).execute()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else Result.Error(IOException("Group details error"))
        } catch (e: ConnectException) {
            Result.Error(e)
        }
    }

    fun setMark(user: LoggedInUser, lessonId: Long, studentId: Long, mark: Double?): Result<Unit> {
        return try {
            val request = SetMarkRequest(studentId, mark)
            val response = RetrofitClient.apiInterface.setMark("Bearer ${user.access}", lessonId, request).execute()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else Result.Error(IOException("Set mark error"))
        } catch (e: ConnectException) {
            Result.Error(e)
        }
    }

    fun deleteMark(user: LoggedInUser, lessonId: Long, studentId: Long): Result<Unit> {
        val request = DeleteMarkRequest(studentId)
        val response = RetrofitClient.apiInterface.deleteMark("Bearer ${user.access}", lessonId, request).execute()
        return if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else Result.Error(IOException("Delete mark error"))
    }

    fun setAttendance(user: LoggedInUser, lessonId: Long, studentId: Long, attendance: Boolean): Result<Unit> {
        return try {
            val request = SetAttendanceRequest(studentId, attendance)
            val response = RetrofitClient.apiInterface.setAttendance("Bearer ${user.access}", lessonId, request).execute()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else Result.Error(IOException("Set attendance error"))
        } catch (e: ConnectException) {
            Result.Error(e)
        }
    }

    fun createGroup(user: LoggedInUser, title: String, subject: String): Result<Group> {
        return try {
            val request = CreateGroupRequest(title, subject)
            val response = RetrofitClient.apiInterface.createGroup("Bearer ${user.access}", request).execute()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else Result.Error(IOException("Create group error"))
        } catch (e: ConnectException) {
            Result.Error(e)
        }
    }

    fun createLesson(user: LoggedInUser, title: String, groupId: Long): Result<Lesson> {
        return try {
            val request = CreateLessonRequest(title)
            val response = RetrofitClient.apiInterface.createLesson("Bearer ${user.access}", groupId, request).execute()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else Result.Error(IOException("Create lesson error"))
        } catch (e: ConnectException) {
            Result.Error(e)
        }
    }

    fun addStudent(user: LoggedInUser, email: String, groupId: Long): Result<Group> {
        return try {
            val request = AddStudentRequest(email)
            val response = RetrofitClient.apiInterface.addStudent("Bearer ${user.access}", groupId, request).execute()
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else Result.Error(IOException("Create lesson error"))
        } catch (e: ConnectException) {
            Result.Error(e)
        }
    }

}