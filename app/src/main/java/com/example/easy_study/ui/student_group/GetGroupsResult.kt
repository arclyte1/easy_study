package com.example.easy_study.ui.student_group

import com.example.easy_study.data.model.Group

data class GetGroupsResult(
    val success: List<Group>? = null,
    val error: Int? = null
)