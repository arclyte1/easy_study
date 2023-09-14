package com.example.easy_study.presentation.shared

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easy_study.presentation.ui.theme.EasyStudyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigningTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    label: String = "",
    errorMessage: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    readOnly: Boolean = false,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(15.dp),
        singleLine = true,
        supportingText = { Text(errorMessage, color = Color(0xffb3261e)) },
        isError = isError,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        readOnly = readOnly
    )
}

@Composable
@Preview
fun RegistrationTextFieldPreview() {
    var text by remember { mutableStateOf("text") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    EasyStudyTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            SigningTextField(
                value = text,
                onValueChange = {
                    text = it
                    isError = !isError
                    errorMessage = if (isError) "Invalid login" else ""
                },
                isError = isError,
                modifier = Modifier.fillMaxWidth(),
                label = "Login",
                errorMessage = errorMessage
            )
        }
    }
}