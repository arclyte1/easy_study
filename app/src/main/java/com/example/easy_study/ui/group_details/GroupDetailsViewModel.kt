package com.example.easy_study.ui.group_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easy_study.R
import com.example.easy_study.data.GroupRepository
import com.example.easy_study.data.Result
import com.example.easy_study.data.model.Lesson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupDetailsViewModel() : ViewModel() {

    private val groupRepository = GroupRepository.instance

    private val _getLessonsResult = MutableLiveData<GetGroupDetailsResult>()
    val getLessonsResult: LiveData<GetGroupDetailsResult> = _getLessonsResult

    private val _gettingLessons = MutableLiveData<Boolean>()
    val gettingLessons: LiveData<Boolean> = _gettingLessons

    fun getStudentGroupDetails(groupId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            _gettingLessons.postValue(true)
            val result = groupRepository.getStudentGroupDetails(groupId)
            _gettingLessons.postValue(false)

            if (result is Result.Success) {
                _getLessonsResult.postValue(GetGroupDetailsResult(success = result.data))
            } else {
                _getLessonsResult.postValue(GetGroupDetailsResult(error = R.string.get_group_details_failed))
            }
        }
    }

    fun setCurrentLesson(lesson: Lesson) {
        groupRepository.currentLesson = lesson
    }

    fun addLesson(title: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = groupRepository.addLesson(title)
            if (result is Result.Success)
                _getLessonsResult.postValue(GetGroupDetailsResult(success = result.data))
        }
    }

}