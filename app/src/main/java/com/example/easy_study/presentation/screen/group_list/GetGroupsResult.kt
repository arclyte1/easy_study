package com.example.easy_study.presentation.screen.group_list

import com.example.easy_study.domain.model.Group

data class GetGroupsResult(
    val success: List<Group>? = null,
    val error: String? = null
)