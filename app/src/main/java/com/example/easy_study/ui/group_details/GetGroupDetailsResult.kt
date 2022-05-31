package com.example.easy_study.ui.group_details

import com.example.easy_study.data.model.Lesson

class GetGroupDetailsResult(
    val success: List<Lesson>? = null,
    val error: Int? = null
)