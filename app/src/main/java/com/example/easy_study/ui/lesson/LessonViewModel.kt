package com.example.easy_study.ui.lesson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easy_study.data.GroupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LessonViewModel : ViewModel() {

    private val groupRepository = GroupRepository.instance

    private val _studentList = MutableLiveData<List<LessonUser>>()
    val studentList: LiveData<List<LessonUser>> = _studentList

    fun getStudentList() {
        CoroutineScope(Dispatchers.IO).launch {
            val group = groupRepository.currentGroup
            val lesson = groupRepository.currentLesson
            val students: MutableList<LessonUser> = mutableListOf()

            for (user in group?.students!!) {
                val attendanceCheck = lesson?.attendances?.find {
                    it.id == user.id
                }
                val mark = lesson?.marks?.find {
                    it.student.id == user.id
                }
                val student = LessonUser(user.id,
                    user.name,
                    attendanceCheck != null,
                    mark?.value)
                students.add(student)
            }

            _studentList.postValue(students)
        }
    }

    private fun updateStudents() {
        groupRepository.updateCurrentGroup()
    }

    fun setMark(user: LessonUser, mark: Double?) {
        CoroutineScope(Dispatchers.IO).launch {
            groupRepository.setMark(user.id, mark)
            updateStudents()
        }
    }

    fun setAttendance(user: LessonUser, attendance: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            groupRepository.setAttendance(user.id, attendance)
            updateStudents()
        }
    }

    fun addStudent(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            groupRepository.addStudent(email)
            getStudentList()
        }
    }

}