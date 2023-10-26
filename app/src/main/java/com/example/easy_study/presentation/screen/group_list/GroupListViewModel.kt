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
import com.example.easy_study.domain.use_case.GetLoggedInUserUseCase
import com.example.easy_study.presentation.navigation.AppNavigator
import com.example.easy_study.presentation.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor (
    private val getGroupsUseCase: GetGroupsUseCase,
    private val addGroupUseCase: AddGroupUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    private val _screenState = MutableStateFlow(GroupListState())
    val screenState: StateFlow<GroupListState> = _screenState

    init {
        getGroups()
        getLoggedInUserUseCase()?.let { user ->
            _screenState.update {
                it.copy(
                    userRole = user.role
                )
            }
        }
    }

    fun getGroups() {
        getGroupsUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _screenState.update {
                        it.copy(
                            isLoading = false,
                            groupList = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Error -> {
                    _screenState.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
                is Resource.Loading -> {
                    _screenState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addGroup(title: String, subject: String) {
        addGroupUseCase(title, subject).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _screenState.update {
                        it.copy(
                            isCreatingGroup = false,
                            groupList = result.data ?: emptyList(),
                            isGroupCreatingDialogVisible = false,
                        )
                    }
                }
                is Resource.Error -> {
                    _screenState.update {
                        it.copy(
                            isCreatingGroup = false,
                            isGroupCreatingDialogVisible = false,
                        )
                    }
                }
                is Resource.Loading -> {
                    _screenState.update {
                        it.copy(
                            isCreatingGroup = true,
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun openGroup(groupId: Long, groupTitle: String) {
        appNavigator.tryNavigateTo(Destination.LessonListScreen(groupId, groupTitle))
    }

    fun showGroupCreationDialog() {
        _screenState.update {
            it.copy(
                isGroupCreatingDialogVisible = true
            )
        }
    }

    fun dismissGroupCreationDialog() {
        if (!_screenState.value.isCreatingGroup) {
            _screenState.update {
                it.copy(
                    isGroupCreatingDialogVisible = false
                )
            }
        }
    }

    fun isTitleValid(title: String): Boolean {
        return title.trim().length > 5
    }
}