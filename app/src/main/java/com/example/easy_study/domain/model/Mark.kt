package com.example.easy_study.domain.model

import com.google.gson.annotations.SerializedName

data class Mark(
    val id: Long,
    val student: User,
    @SerializedName("lesson")
    val lessonId: Long,
    @SerializedName("mark")
    val value: Double?,
)