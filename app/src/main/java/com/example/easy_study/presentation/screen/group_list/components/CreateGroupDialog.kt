package com.example.easy_study.presentation.screen.group_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.easy_study.R
import com.example.easy_study.common.conditional
import com.example.easy_study.common.drawAnimatedBorder
import com.example.easy_study.presentation.shared.SigningTextField
import com.example.easy_study.presentation.ui.theme.EasyStudyTheme

@Composable
fun CreateGroupDialog(
    createGroup: (title: String, subject: String) -> Unit,
    onDismissRequest: () -> Unit,
    validateTitle: (String) -> Boolean,
    isCreating: Boolean,
) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var isTitleValid by remember { mutableStateOf(true) }
    var canCreate by remember { mutableStateOf(false) }
    val animationColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.primary,
    )

    Dialog(onDismissRequest = {
        if (!isCreating) onDismissRequest()
    }) {
        Surface(
            modifier = Modifier.clip(RoundedCornerShape(15.dp))
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.group_creating),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                )
                SigningTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        isTitleValid = validateTitle(title)
                        canCreate = isTitleValid
                    },
                    isError = !isTitleValid,
                    errorMessage = if (isTitleValid) "" else stringResource(id = R.string.invalid_title),
                    label = stringResource(id = R.string.title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp),
                    readOnly = isCreating
                )
                SigningTextField(
                    value = subject,
                    onValueChange = {
                        subject = it
                    },
                    label = stringResource(id = R.string.subject),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    readOnly = isCreating
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            createGroup(title, subject)
                        },
                        enabled = canCreate && !isCreating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .conditional(isCreating) {
                                drawAnimatedBorder(
                                    2.dp,
                                    CircleShape,
                                    { Brush.sweepGradient(animationColors) },
                                    2000
                                )
                            }
                    ) {
                        Text(
                            text = stringResource(id = R.string.create)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CreateGroupDialogPreview() {
    EasyStudyTheme {
        CreateGroupDialog({_, _ ->}, {}, {it.length > 5}, false)
    }
}