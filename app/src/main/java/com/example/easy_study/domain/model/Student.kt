package com.example.easy_study.domain.model

data class Student(
    val id: Long,
    val email: String,
    val name: String,
    var attendance: Boolean,
    var mark: Double?,
)