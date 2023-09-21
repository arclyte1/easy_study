package com.example.easy_study.presentation.screen.registration.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.easy_study.common.conditional
import com.example.easy_study.presentation.shared.SigningTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigningDropdownMenu(
    options: List<String>,
    optionSelected: (index: Int) -> Boolean,
    modifier: Modifier = Modifier,
    selectedOptionIndex: Int? = null,
    label: String = "",
    defaultValue: String = "",
    canInteract: Boolean = true
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = it
        },
        modifier = modifier
    ) {
        SigningTextField(
            value = selectedOptionIndex?.let { options[selectedOptionIndex] } ?: defaultValue,
            label = label,
            onValueChange = {},
            modifier = Modifier
                .conditional(canInteract) {
                    menuAnchor()
                }
                .fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded
                )
            },
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            },
            modifier = Modifier.exposedDropdownSize()
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Text(text = option, modifier = Modifier.fillMaxWidth())
                    },
                    onClick = {
                        isExpanded = optionSelected(index)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}