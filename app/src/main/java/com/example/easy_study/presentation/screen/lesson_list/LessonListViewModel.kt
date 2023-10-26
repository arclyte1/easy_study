package com.example.easy_study.presentation.screen.lesson_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easy_study.common.Resource
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.domain.model.LoggedInUser
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.domain.use_case.*
import com.example.easy_study.presentation.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LessonListViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val getLessonListUseCase: GetLessonListUseCase,
    private val addLessonUseCase: AddLessonUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
) : ViewModel() {

    private val groupId: Long = (checkNotNull(savedStateHandle[Destination.LessonListScreen.GROUP_ID_KEY]) as String).toLong()
    private val groupTitle: String = checkNotNull(savedStateHandle[Destination.LessonListScreen.GROUP_TITLE_KEY])
    private val loggedInUser: LoggedInUser? = getLoggedInUserUseCase()

    private val _screenState = MutableStateFlow(LessonListState())
    val screenState: StateFlow<LessonListState> = _screenState

    init {
        _screenState.update {
            it.copy(
                groupTitle = groupTitle,
                userRole = loggedInUser?.role ?: UserRole.STUDENT
            )
        }
        getLessonList(groupId)
    }

    fun getLessonList(groupId: Long) {
        getLessonListUseCase(groupId).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _screenState.update {
                        it.copy(
                            lessonList = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _screenState.update {
                        it.copy(
                            isLoading = false
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

    fun addLesson(title: String) {
        addLessonUseCase(groupId, title).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _screenState.update {
                        it.copy(
                            isCreatingLesson = false,
                            lessonList = result.data ?: emptyList(),
                            isLessonCreatingDialogVisible = false
                        )
                    }
                }
                is Resource.Error -> {
                    _screenState.update {
                        it.copy(
                            isCreatingLesson = false,
                            isLessonCreatingDialogVisible = false
                        )
                    }
                }
                is Resource.Loading -> {
                    _screenState.update {
                        it.copy(
                            isCreatingLesson = true
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun openLesson(lessonId: Long) {

    }

    fun showLessonCreationDialog() {
        _screenState.update {
            it.copy(
                isLessonCreatingDialogVisible = true
            )
        }
    }

    fun dismissLessonCreationDialog() {
        if (!_screenState.value.isCreatingLesson) {
            _screenState.update {
                it.copy(
                    isLessonCreatingDialogVisible = false
                )
            }
        }
    }

    fun isTitleValid(title: String): Boolean {
        return title.trim().length > 5
    }
}