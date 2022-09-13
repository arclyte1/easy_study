package com.example.easy_study.domain.model

data class Group(
    val id: Long,
    val group_title: String,
    val subject_title: String,
    val students: List<User>,
    val teachers: List<User>,
)