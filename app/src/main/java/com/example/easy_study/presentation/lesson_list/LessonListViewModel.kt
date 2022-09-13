package com.example.easy_study.presentation.lesson_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easy_study.common.Resource
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LessonListViewModel @Inject constructor (
    private val getLessonListUseCase: GetLessonListUseCase,
    private val addLessonUseCase: AddLessonUseCase,
) : ViewModel() {


    private val _getLessonsResult = MutableLiveData<GetLessonListResult>()
    val getLessonsResult: LiveData<GetLessonListResult> = _getLessonsResult

    private val _gettingLessons = MutableLiveData<Boolean>()
    val gettingLessons: LiveData<Boolean> = _gettingLessons

    fun getLessonList(groupId: Long) {
        getLessonListUseCase(groupId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _gettingLessons.postValue(false)
                    _getLessonsResult.postValue(GetLessonListResult(success = result.data))
                }
                is Resource.Error -> {
                    _gettingLessons.postValue(false)
                    _getLessonsResult.postValue(GetLessonListResult(error = result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _gettingLessons.postValue(true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addLesson(groupId: Long, title: String) {
        addLessonUseCase(groupId, title).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _gettingLessons.postValue(false)
                    _getLessonsResult.postValue(GetLessonListResult(success = result.data))
                }
                is Resource.Error -> {
                    _gettingLessons.postValue(false)
                    _getLessonsResult.postValue(GetLessonListResult(error = result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _gettingLessons.postValue(true)
                }
            }
        }.launchIn(viewModelScope)
    }

}