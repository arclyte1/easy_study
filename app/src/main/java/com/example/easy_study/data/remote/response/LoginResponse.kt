package com.example.easy_study.data.remote.response

import com.example.easy_study.domain.model.Group
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id")
    val userId: Long,
    val email: String,
    val refresh: String,
    val access: String,
    val name: String,
    val role: String,
    val studying_groups: List<Group>,
    val teaching_groups: List<Group>,
    // TODO marks and attendances
)