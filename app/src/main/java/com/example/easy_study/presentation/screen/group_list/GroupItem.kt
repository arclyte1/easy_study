package com.example.easy_study.presentation.screen.group_list

import com.example.easy_study.domain.model.Group

data class GroupItem(
    val id: Long,
    val groupTitle: String,
    val subjectTitle: String,
) {

    companion object {
        fun fromGroup(group: Group) = GroupItem(
            id = group.id,
            groupTitle = group.group_title,
            subjectTitle = group.subject_title
        )
    }
}