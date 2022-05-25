package com.example.easy_study.ui.lesson

data class LessonUser(
    val id: Long,
    val name: String,
    var attendance: Boolean,
    var mark: Double?,
)