package com.example.easy_study.presentation.screen.lesson_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easy_study.R
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.presentation.screen.lesson_list.components.CreateLessonDialog
import com.example.easy_study.presentation.screen.lesson_list.components.LessonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonListScreen(
    screenState: LessonListState,
    openLesson: (lessonId: Long) -> Unit,
    createLesson: (title: String) -> Unit,
    validateTitle: (String) -> Boolean,
    showLessonCreationDialog: () -> Unit,
    dismissLessonCreationDialog: () -> Unit,
) {
    if (screenState.isLessonCreatingDialogVisible)
        CreateLessonDialog(
            createLesson = createLesson,
            onDismissRequest = dismissLessonCreationDialog,
            validateTitle = validateTitle,
            screenState.isCreatingLesson
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.lessons),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = screenState.groupTitle,
                        )
                    }
                },
                modifier = Modifier.shadow(8.dp)
            )
        },
        floatingActionButton = {
            if (screenState.userRole == UserRole.TEACHER) {
                FloatingActionButton(onClick = {
                    showLessonCreationDialog()
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
            if (screenState.lessonList.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_lessons),
                    fontSize = 24.sp,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    textAlign = TextAlign.Center
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(screenState.lessonList) { item ->
                    LessonCard(
                        item = item,
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 8.dp
                            )
                            .clickable {
                                openLesson(item.id)
                            }
                    )
                }
            }
        }
    }
}