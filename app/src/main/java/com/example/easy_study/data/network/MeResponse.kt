package com.example.easy_study.data.network

import com.example.easy_study.data.model.Group
import com.example.easy_study.data.model.UserRole
import com.google.gson.annotations.SerializedName

class MeResponse(
    @SerializedName("id")
    val userId: Long,
    val email: String,
    val refresh: String,
    val access: String,
    val name: String,
    val role: UserRole.Role,
    val studying_groups: List<Group>,
    val teaching_groups: List<Group>,
    // TODO marks and attendances
)