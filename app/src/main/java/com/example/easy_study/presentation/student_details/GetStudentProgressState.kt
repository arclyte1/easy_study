package com.example.easy_study.presentation.student_details

import com.example.easy_study.domain.model.Lesson

data class GetStudentProgressState(
    val isLoading: Boolean = false,
    val lessons: List<Lesson>? = null,
    val error: String = ""
)