package com.example.easy_study.data.remote.request

import com.google.gson.annotations.SerializedName

data class CreateGroupRequest(
    @SerializedName("group_title")
    val title: String,
    @SerializedName("subject_title")
    val subject: String,
)