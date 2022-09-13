package com.example.easy_study.domain.repository

import com.example.easy_study.domain.model.Group
import com.example.easy_study.domain.model.Lesson
import com.example.easy_study.domain.model.Student

interface GroupRepository {

    suspend fun getGroups(): List<Group>

    suspend fun getLessonList(groupId: Long): List<Lesson>

    suspend fun getStudentList(lessonId: Long): List<Student>

    suspend fun getGroupDetails(groupId: Long): Group?

    suspend fun getStudentProgress(groupId: Long, email: String): List<Lesson>

    suspend fun setMark(lessonId: Long, userId: Long, mark: Double?)

    suspend fun setAttendance(lessonId: Long, userId: Long, attendance: Boolean)

    suspend fun addGroup(title: String, subject: String): List<Group>

    suspend fun addLesson(groupId: Long, title: String): List<Lesson>

    suspend fun addStudent(groupId: Long, email: String): Group

    suspend fun addTeacher(groupId: Long, email: String): Group

}