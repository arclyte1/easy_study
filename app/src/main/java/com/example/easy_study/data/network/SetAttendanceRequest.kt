package com.example.easy_study.data.network

import com.google.gson.annotations.SerializedName

class SetAttendanceRequest(
    @SerializedName("student")
    val userId: Long,
    val attendance: Boolean,
)