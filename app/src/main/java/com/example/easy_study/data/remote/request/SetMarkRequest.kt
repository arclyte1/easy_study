package com.example.easy_study.data.remote.request

import com.google.gson.annotations.SerializedName

data class SetMarkRequest(
    @SerializedName("student")
    val userId: Long,
    val mark: Double?,
)