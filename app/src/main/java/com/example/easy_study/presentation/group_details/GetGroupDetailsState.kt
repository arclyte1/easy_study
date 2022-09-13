package com.example.easy_study.presentation.group_details

import com.example.easy_study.domain.model.Group

data class GetGroupDetailsState(
    val isLoading: Boolean = false,
    val group: Group? = null,
    val error: String = ""
)