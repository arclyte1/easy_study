package com.example.easy_study.presentation.screen.lesson_list

import com.example.easy_study.domain.model.Lesson
import com.example.easy_study.domain.model.UserRole

data class LessonListState(
    val lessonList: List<Lesson> = emptyList(),
    val isLoading: Boolean = false,
    val isLessonCreatingDialogVisible: Boolean = false,
    val userRole: UserRole = UserRole.STUDENT,
    val isCreatingLesson: Boolean = false,
    val groupTitle: String = "",
)
