package com.example.easy_study.data

import com.example.easy_study.data.model.Group
import com.example.easy_study.data.model.Lesson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class GroupRepository(private val dataSource: GroupDataSource) {

    private val loginRepository = LoginRepository.instance

    var groups: List<Group>? = null
        private set

    var currentGroup: Group? = null

    var currentLesson: Lesson? = null

    fun getGroups(): Result<List<Group>> {
        return if (loginRepository.isLoggedIn) {
            val result = dataSource.getGroups(loginRepository.user!!)
            if (result is Result.Success) {
                setGroups(result.data)
            }
            result
        } else Result.Error(IOException("User not logged in"))
    }

    fun getStudentGroupDetails(groupId: Long): Result<List<Lesson>> {
        return if (loginRepository.isLoggedIn) {
            val result = dataSource.getStudentGroupDetails(loginRepository.user!!, groupId)
            result
        } else Result.Error(IOException("User not logged in"))
    }

    private fun setGroups(groups: List<Group>) {
        this.groups = groups
    }

    fun setMark(userId: Long, mark: Double?) {
        //if (mark != null)
            dataSource.setMark(loginRepository.user!!, currentLesson!!.id, userId, mark)
        //else
        //    dataSource.deleteMark(loginRepository.user!!, currentLesson!!.id, userId)
    }

    fun setAttendance(userId: Long, attendance: Boolean) {
        dataSource.setAttendance(loginRepository.user!!, currentLesson!!.id, userId, attendance)
    }

    fun updateCurrentGroup() {
        getStudentGroupDetails(currentGroup!!.id)
    }

    fun addGroup(title: String, subject: String): Result<List<Group>> {
        val result = dataSource.createGroup(loginRepository.user!!, title, subject)
        return if (result is Result.Success) {
            val groupsResult = getGroups()
            if (groupsResult is Result.Success)
                setGroups(groupsResult.data)
            groupsResult
        } else
            Result.Error(IOException("Failed to create group"))
    }

    fun addLesson(title: String): Result<List<Lesson>> {
        val result = dataSource.createLesson(loginRepository.user!!, title, currentGroup!!.id)
        return if (result is Result.Success) {
            dataSource.getStudentGroupDetails(loginRepository.user!!, currentGroup!!.id)
        } else
            Result.Error(IOException("Failed to create lesson"))
    }

    fun addStudent(email: String): Result<Unit> {
        val result = dataSource.addStudent(loginRepository.user!!, email, currentGroup!!.id)
        return if (result is Result.Success) {
            currentGroup = result.data
            CoroutineScope(Dispatchers.IO).launch {
                getGroups()
            }
            Result.Success(Unit)
        } else
            Result.Error(IOException("Failed to add student"))
    }

    companion object {
        val instance = GroupRepository(GroupDataSource())
    }
}