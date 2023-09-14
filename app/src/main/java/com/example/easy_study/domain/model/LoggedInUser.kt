package com.example.easy_study.domain.model

data class LoggedInUser(
    val userId: Long,
    val email: String,
    val refresh: String,
    val access: String,
    val name: String,
    val role: UserRole,
    val studying_groups: List<Group>,
    val teaching_groups: List<Group>,
)