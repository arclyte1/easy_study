package com.example.easy_study.data.network

import com.google.gson.annotations.SerializedName

data class SetMarkRequest(
    @SerializedName("student")
    val userId: Long,
    val mark: Double?,
)