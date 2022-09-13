package com.example.easy_study.data.repository

import com.example.easy_study.data.remote.EasyStudyApi
import com.example.easy_study.data.remote.request.*
import com.example.easy_study.domain.model.Group
import com.example.easy_study.domain.model.Lesson
import com.example.easy_study.domain.model.Student
import com.example.easy_study.domain.repository.GroupRepository
import com.example.easy_study.domain.repository.LoginRepository
import java.io.IOException
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor (
    private val api: EasyStudyApi,
    private val loginRepository: LoginRepository
): GroupRepository {


    override suspend fun getGroups(): List<Group> {
        return if (loginRepository.getLoggedInUser() != null) {
            val response = api.getGroups(
                "Bearer ${loginRepository.getLoggedInUser()!!.access}"
            )
            //groupList = response
            response
        } else throw IOException("User not logged in")
    }

    override suspend fun getLessonList(groupId: Long): List<Lesson> {
        return if (loginRepository.getLoggedInUser() != null) {
            api.getLessonList(
                "Bearer ${loginRepository.getLoggedInUser()!!.access}",
                groupId
            )
        } else throw IOException("User not logged in")
    }

    override suspend fun getStudentList(lessonId: Long): List<Student> {
        return if (loginRepository.getLoggedInUser() != null) {
            api.getStudentList(
                "Bearer ${loginRepository.getLoggedInUser()!!.access}",
                lessonId
            )
        } else throw IOException("User not logged in")
    }

    override suspend fun getGroupDetails(groupId: Long): Group? {
        val groups = getGroups()
        return groups.find { it.id == groupId }
    }

    override suspend fun getStudentProgress(groupId: Long, email: String): List<Lesson> {
        return api.getStudentProgress(
            "Bearer ${loginRepository.getLoggedInUser()!!.access}",
            groupId,
            email
        )
    }

    override suspend fun setMark(lessonId: Long, userId: Long, mark: Double?) {
        val request = SetMarkRequest(
            userId,
            mark
        )
        api.setMark(
            "Bearer ${loginRepository.getLoggedInUser()!!.access}",
            lessonId,
            request
        )
    }

    override suspend fun setAttendance(lessonId: Long, userId: Long, attendance: Boolean) {
        val request = SetAttendanceRequest(
            userId,
            attendance
        )
        api.setAttendance(
            "Bearer ${loginRepository.getLoggedInUser()!!.access}",
            lessonId,
            request
        )
    }

    override suspend fun addGroup(title: String, subject: String): List<Group> {
        val request = CreateGroupRequest(
            title,
            subject
        )
        api.createGroup(
            "Bearer ${loginRepository.getLoggedInUser()!!.access}",
            request
        )
        return getGroups()
    }

    override suspend fun addLesson(groupId: Long, title: String): List<Lesson> {
        val request = CreateLessonRequest(title)
        api.createLesson(
            "Bearer ${loginRepository.getLoggedInUser()!!.access}",
            groupId,
            request
        )
        return getLessonList(groupId)
    }

    override suspend fun addStudent(groupId: Long, email: String): Group {
        val request = AddStudentRequest(email)
        return api.addStudent(
            "Bearer ${loginRepository.getLoggedInUser()!!.access}",
            groupId,
            request
        )
    }

    override suspend fun addTeacher(groupId: Long, email: String): Group {
        val request = AddTeacherRequest(email)
        return api.addTeacher(
            "Bearer ${loginRepository.getLoggedInUser()!!.access}",
            groupId,
            request
        )
    }
}