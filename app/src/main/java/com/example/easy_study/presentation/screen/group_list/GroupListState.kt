package com.example.easy_study.presentation.screen.group_list

import com.example.easy_study.domain.model.Group
import com.example.easy_study.domain.model.UserRole

data class GroupListState(
    val groupList: List<Group> = emptyList(),
    val isGroupCreatingDialogVisible: Boolean = false,
    val userRole: UserRole = UserRole.STUDENT,
    val isCreatingGroup: Boolean = false,
    val isLoading: Boolean = false
)
