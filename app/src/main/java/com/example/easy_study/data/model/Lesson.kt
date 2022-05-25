package com.example.easy_study.data.model

import com.google.gson.annotations.SerializedName

data class Lesson(
    val id: Long,
    val title: String,
    val date: String?,
    @SerializedName("group")
    val groupId: Long,

    // for students
    val attendance: Boolean?,
    val mark: Double?,

    // for teachers
    val attendances: List<User>?,
    val marks: List<Mark>?,
)