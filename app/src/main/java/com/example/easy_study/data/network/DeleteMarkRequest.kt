package com.example.easy_study.data.network

import com.google.gson.annotations.SerializedName

data class DeleteMarkRequest(
    @SerializedName("student")
    val userId: Long
)