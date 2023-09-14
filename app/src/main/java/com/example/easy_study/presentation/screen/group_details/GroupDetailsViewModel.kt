package com.example.easy_study.presentation.screen.group_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easy_study.common.Resource
import com.example.easy_study.domain.use_case.AddStudentUseCase
import com.example.easy_study.domain.use_case.AddTeacherUseCase
import com.example.easy_study.domain.use_case.GetGroupDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    private val getGroupDetailsUseCase: GetGroupDetailsUseCase,
    private val addStudentUseCase: AddStudentUseCase,
    private val addTeacherUseCase: AddTeacherUseCase
) : ViewModel() {

    private val _getGroupDetailsState = MutableStateFlow(GetGroupDetailsState())
    val getGroupDetailsState: StateFlow<GetGroupDetailsState> = _getGroupDetailsState

    fun getGroupDetails(groupId: Long) {
        getGroupDetailsUseCase(groupId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _getGroupDetailsState.value =
                        GetGroupDetailsState(
                            group = result.data
                        )
                }
                is Resource.Error -> {
                    _getGroupDetailsState.value =
                        GetGroupDetailsState(
                            error = result.message ?: "Unknown error"
                        )
                }
                is Resource.Loading -> {
                    _getGroupDetailsState.value =
                        GetGroupDetailsState(
                            isLoading = true
                        )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addStudent(groupId: Long, email: String) {
        addStudentUseCase(groupId, email).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _getGroupDetailsState.value =
                        GetGroupDetailsState(
                            group = result.data
                        )
                }
                is Resource.Error -> {
                    _getGroupDetailsState.value =
                        GetGroupDetailsState(
                            error = result.message ?: "Unknown error"
                        )
                }
                is Resource.Loading -> {
                    _getGroupDetailsState.value =
                        GetGroupDetailsState(
                            isLoading = true
                        )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addTeacher(groupId: Long, email: String) {
        addTeacherUseCase(groupId, email).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _getGroupDetailsState.value =
                        GetGroupDetailsState(
                            group = result.data
                        )
                }
                is Resource.Error -> {
                    _getGroupDetailsState.value =
                        GetGroupDetailsState(
                            error = result.message ?: "Unknown error"
                        )
                }
                is Resource.Loading -> {
                    _getGroupDetailsState.value =
                        GetGroupDetailsState(
                            isLoading = true
                        )
                }
            }
        }.launchIn(viewModelScope)
    }

}