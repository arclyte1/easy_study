package com.example.easy_study.data.remote.request

import com.google.gson.annotations.SerializedName

class SetAttendanceRequest(
    @SerializedName("student")
    val userId: Long,
    val attendance: Boolean,
)