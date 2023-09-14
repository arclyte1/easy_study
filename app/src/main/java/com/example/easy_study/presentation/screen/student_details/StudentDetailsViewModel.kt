package com.example.easy_study.presentation.screen.student_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easy_study.common.Resource
import com.example.easy_study.domain.use_case.GetStudentProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StudentDetailsViewModel @Inject constructor(
    private val getStudentProgressUseCase: GetStudentProgressUseCase
) : ViewModel() {

    private val _getStudentProgressState = MutableStateFlow(GetStudentProgressState())
    val getStudentProgressState: StateFlow<GetStudentProgressState> = _getStudentProgressState

    fun getStudentDetails(groupId: Long, email: String) {
        getStudentProgressUseCase(groupId, email).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _getStudentProgressState.value = GetStudentProgressState(lessons = result.data)
                }
                is Resource.Error -> {
                    _getStudentProgressState.value = GetStudentProgressState(error = result.message ?: "Unknown error")
                }
                is Resource.Loading -> {
                    _getStudentProgressState.value = GetStudentProgressState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}