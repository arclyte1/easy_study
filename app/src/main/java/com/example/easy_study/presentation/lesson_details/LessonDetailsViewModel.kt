package com.example.easy_study.presentation.lesson_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easy_study.common.Resource
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.domain.model.Student
import com.example.easy_study.domain.use_case.*
import com.example.easy_study.presentation.group_list.GetGroupsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonDetailsViewModel @Inject constructor (
    private val getStudentListUseCase: GetStudentListUseCase,
    private val setMarkUseCase: SetMarkUseCase,
    private val setAttendanceUseCase: SetAttendanceUseCase,
    private val addStudentUseCase: AddStudentUseCase
) : ViewModel() {


    private val _studentList = MutableLiveData<List<Student>>()
    val studentList: LiveData<List<Student>> = _studentList

    fun getStudentList(lessonId: Long) {
        getStudentListUseCase(lessonId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _studentList.postValue(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    // error!!
                }
                is Resource.Loading -> {
                    // loading
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setMark(lessonId: Long, user: Student, mark: Double?) {
        setMarkUseCase(lessonId, user.id, mark).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    getStudentList(lessonId)
                }
                is Resource.Error -> {
                    // error!!
                }
                is Resource.Loading -> {
                    // loading
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setAttendance(lessonId: Long, user: Student, attendance: Boolean) {
        setAttendanceUseCase(lessonId, user.id, attendance).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    getStudentList(lessonId)
                }
                is Resource.Error -> {
                    // error!!
                }
                is Resource.Loading -> {
                    // loading
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addStudent(lessonId: Long, email: String) {
        addStudentUseCase(lessonId, email).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    getStudentList(lessonId)
                }
                is Resource.Error -> {
                    // error!!
                }
                is Resource.Loading -> {
                    // loading
                }
            }
        }.launchIn(viewModelScope)
    }

}