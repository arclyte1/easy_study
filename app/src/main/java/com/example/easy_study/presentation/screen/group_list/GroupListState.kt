package com.example.easy_study.presentation.screen.group_list

data class GroupListState(
    val groupList: List<GroupItem> = emptyList(),
    val isLoading: Boolean = false
)
