package com.example.easy_study.presentation.screen.lesson_list

import com.example.easy_study.domain.model.Lesson

class GetLessonListResult(
    val success: List<Lesson>? = null,
    val error: String? = null
)