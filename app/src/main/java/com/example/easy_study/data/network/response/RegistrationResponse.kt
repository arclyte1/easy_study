package com.example.easy_study.data.network.response

import com.example.easy_study.data.model.Group
import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
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