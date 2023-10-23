package com.example.easy_study.presentation.screen.group_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easy_study.R
import com.example.easy_study.domain.model.Group
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.presentation.screen.group_list.components.CreateGroupDialog
import com.example.easy_study.presentation.screen.group_list.components.GroupCard
import com.example.easy_study.presentation.ui.theme.EasyStudyTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListScreen(
    screenState: GroupListState,
    openGroup: (groupId: Long) -> Unit,
    createGroup: ((title: String, subject: String) -> Unit),
    validateTitle: (String) -> Boolean,
    showGroupCreationDialog: () -> Unit,
    dismissGroupCreationDialog: () -> Unit,
) {
    if (screenState.isGroupCreatingDialogVisible)
        CreateGroupDialog(
            createGroup = createGroup,
            onDismissRequest = dismissGroupCreationDialog,
            validateTitle = validateTitle,
            screenState.isCreatingGroup
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.my_groups),
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = Modifier.shadow(8.dp)
            )
        },
        floatingActionButton = {
            if (screenState.userRole == UserRole.TEACHER) {
                FloatingActionButton(onClick = {
                    showGroupCreationDialog()
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add FAB"
                    )
                }
            }
        }
    ) { scaffoldPaddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddingValues)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(screenState.groupList) { item ->
                    GroupCard(
                        item = item,
                        modifier = Modifier.padding(
                            top = 8.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupListScreenPreview() {
    var screenState by remember {
        mutableStateOf(
            GroupListState(
                groupList = listOf(
                    Group(
                        id = 1,
                        group_title = "Group 1",
                        subject_title = "Subject"
                    ),
                    Group(
                        id = 2,
                        group_title = "Group 2",
                        subject_title = ""
                    )
                )

            )
        )
    }
    EasyStudyTheme {
        GroupListScreen(
            screenState = screenState,
            openGroup = {},
            createGroup = { _, _, -> GlobalScope.launch {
                screenState = screenState.copy(isCreatingGroup = true)
                delay(3000)
                screenState = screenState.copy(
                    isCreatingGroup = false,
                    isGroupCreatingDialogVisible = false
                )
            }},
            validateTitle = {it.length > 5},
            showGroupCreationDialog = {
                screenState = screenState.copy(
                    isGroupCreatingDialogVisible = true
                )
            },
            dismissGroupCreationDialog = {
                if (!screenState.isCreatingGroup) {
                    screenState = screenState.copy(
                        isGroupCreatingDialogVisible = false
                    )
                }
            }
        )
    }
}