package com.example.easy_study.presentation.screen.registration

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.easy_study.R
import com.example.easy_study.common.ValidatingResult
import com.example.easy_study.common.conditional
import com.example.easy_study.common.drawAnimatedBorder
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.presentation.shared.SigningTextField
import com.example.easy_study.presentation.ui.theme.EasyStudyTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    screenState: RegistrationState,
    register: (email: String, username: String, role: UserRole, password: String) -> Unit,
    navigateBack: () -> Unit,
    validateEmail: (String) -> ValidatingResult,
    validateUsername: (String) -> ValidatingResult,
    validatePassword: (String) -> ValidatingResult,
) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf<ValidatingResult>(ValidatingResult.Valid) }
    var username by remember { mutableStateOf("") }
    var isUsernameValid by remember { mutableStateOf<ValidatingResult>(ValidatingResult.Valid) }
    var password by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf<ValidatingResult>(ValidatingResult.Valid) }
    var canSignUp by remember { mutableStateOf(false) }
    val roleOptions = UserRole.values().toList()
    var isExpandedRoleMenu by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    val animationColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.primary,
    )

    fun updateCanSignUp() {
        canSignUp = listOf(isEmailValid, isUsernameValid, isPasswordValid).all {
            it is ValidatingResult.Valid
        } && selectedRole != null && email.isNotEmpty()
    }


    Column(modifier = Modifier.fillMaxSize()) {
        SigningTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = validateEmail(email)
                updateCanSignUp()
            },
            isError = isEmailValid is ValidatingResult.Invalid,
            errorMessage = stringResource(id = isEmailValid.errorStringResId),
            label = stringResource(id = R.string.prompt_email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            readOnly = screenState.isSigningUp
        )
        SigningTextField(
            value = username,
            onValueChange = {
                username = it
                isUsernameValid = validateUsername(username)
                updateCanSignUp()
            },
            isError = isUsernameValid is ValidatingResult.Invalid,
            errorMessage = stringResource(id = isUsernameValid.errorStringResId),
            label = stringResource(id = R.string.prompt_username),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            readOnly = screenState.isSigningUp
        )
        SigningTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordValid = validatePassword(password)
                updateCanSignUp()
            },
            isError = isPasswordValid is ValidatingResult.Invalid,
            errorMessage = stringResource(id = isPasswordValid.errorStringResId),
            label = stringResource(id = R.string.prompt_password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            readOnly = screenState.isSigningUp
        )
        ExposedDropdownMenuBox(
            expanded = isExpandedRoleMenu,
            onExpandedChange = {
                isExpandedRoleMenu = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            SigningTextField(
                value = selectedRole?.let { stringResource(id = UserRole.getRes(it)) } ?: "" ,
                label = stringResource(id = R.string.prompt_role),
                onValueChange = {},
                modifier = Modifier
                    .conditional(!screenState.isSigningUp) {
                        menuAnchor()
                    }
                    .fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isExpandedRoleMenu
                    )
                },
            )
            DropdownMenu(
                expanded = isExpandedRoleMenu,
                onDismissRequest = {
                    isExpandedRoleMenu = false
                },
                modifier = Modifier.exposedDropdownSize()
            ) {
                roleOptions.forEach { role ->
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = UserRole.getRes(role)), modifier = Modifier.fillMaxWidth())
                        },
                        onClick = {
                            selectedRole = role
                            isExpandedRoleMenu = false
                            updateCanSignUp()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        Text(
            text = stringResource(id = R.string.action_go_sign_in),
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp, top = 8.dp)
                .conditional(!screenState.isSigningUp) {
                    clickable { navigateBack() }
                },
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {

            Button(
                onClick = {
                    // selectedRole always not null while canSignUp is true
                    register(email, username, selectedRole!!, password)
                },
                enabled = canSignUp && !screenState.isSigningUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .conditional(screenState.isSigningUp) {
                        drawAnimatedBorder(
                            2.dp,
                            CircleShape,
                            { Brush.sweepGradient(animationColors) },
                            2000
                        )
                    }

            ) {
                Text(
                    text = stringResource(id = R.string.action_sign_up)
                )
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
@Preview
fun RegistrationScreenPreview() {
    var screenState by remember {
        mutableStateOf(RegistrationState())
    }
    EasyStudyTheme {
        RegistrationScreen(
            screenState = screenState,
            register = { _, _, _, _ -> GlobalScope.launch {
                screenState = screenState.copy(isSigningUp = true)
                delay(3000)
                screenState = screenState.copy(isSigningUp = false)
            }},
            navigateBack = {},
            validateEmail = { email ->
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    ValidatingResult.Valid
                else
                    ValidatingResult.Invalid(R.string.invalid_email)
            },
            validateUsername = { username ->
                when {
                    username.length !in 6..20 -> ValidatingResult.Invalid(R.string.invalid_username_length)
                    username.contains("[^a-zA-Z0-9_]+") -> ValidatingResult.Invalid(R.string.invalid_username_content)
                    else -> ValidatingResult.Valid
                }
            },
            validatePassword = { password ->
                if (password.length !in 6..20)
                    ValidatingResult.Invalid(R.string.invalid_password)
                else
                    ValidatingResult.Valid
            }
        )
    }
}