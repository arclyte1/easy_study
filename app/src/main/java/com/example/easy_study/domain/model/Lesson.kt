package com.example.easy_study.domain.model

import com.google.gson.annotations.SerializedName

data class Lesson(
    val id: Long,
    val title: String,
    val date: String? = null,
    @SerializedName("group")
    val groupId: Long,

    // for students
    val attendance: Boolean? = null,
    val mark: Double? = null,

    // for teachers
    val attendances: List<User>? = null,
    val marks: List<Mark>? = null,
)