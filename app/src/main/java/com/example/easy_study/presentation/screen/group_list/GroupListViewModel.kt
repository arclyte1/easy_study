package com.example.easy_study.presentation.screen.group_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easy_study.common.Resource
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.data.Result
import com.example.easy_study.domain.use_case.AddGroupUseCase
import com.example.easy_study.domain.use_case.GetGroupsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor (
    private val getGroupsUseCase: GetGroupsUseCase,
    private val addGroupUseCase: AddGroupUseCase
) : ViewModel() {

    private val _getGroupsResult = MutableLiveData<GetGroupsResult>()
    val getGroupsResult: LiveData<GetGroupsResult> = _getGroupsResult

    private val _gettingGroups = MutableLiveData<Boolean>()
    val gettingGroups: LiveData<Boolean> = _gettingGroups

    fun getGroups() {
        getGroupsUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _gettingGroups.postValue(false)
                    _getGroupsResult.postValue(GetGroupsResult(success = result.data))
                }
                is Resource.Error -> {
                    _gettingGroups.postValue(false)
                    _getGroupsResult.postValue(GetGroupsResult(error = result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _gettingGroups.postValue(true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addGroup(title: String, subject: String) {
        addGroupUseCase(title, subject).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _gettingGroups.postValue(false)
                    _getGroupsResult.postValue(GetGroupsResult(success = result.data))
                }
                is Resource.Error -> {
                    _gettingGroups.postValue(false)
                    _getGroupsResult.postValue(GetGroupsResult(error = result.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    _gettingGroups.postValue(true)
                }
            }
        }.launchIn(viewModelScope)
    }

}