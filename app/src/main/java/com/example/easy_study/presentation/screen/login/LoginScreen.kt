package com.example.easy_study.presentation.screen.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.easy_study.R
import com.example.easy_study.common.conditional
import com.example.easy_study.common.drawAnimatedBorder
import com.example.easy_study.presentation.shared.SigningTextField
import com.example.easy_study.presentation.ui.theme.EasyStudyTheme

@Composable
fun LoginScreen(
    screenState: LoginState,
    login: (String, String) -> Unit,
    navigateToRegistration: () -> Unit,
    validateEmail: (String) -> Boolean,
    validatePassword: (String) -> Boolean,
) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(true) }
    var canSignIn by remember { mutableStateOf(false) }
    val animationColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.primary,
    )

    
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "Icon",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
                .height(200.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 32.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        SigningTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = validateEmail(email)
                canSignIn = isEmailValid && isPasswordValid
            },
            isError = !isEmailValid,
            errorMessage = if (isEmailValid) "" else stringResource(id = R.string.invalid_email),
            label = stringResource(id = R.string.prompt_email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            readOnly = screenState.isLoggingIn
        )
        SigningTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordValid = validatePassword(password)
                canSignIn = isEmailValid && isPasswordValid
            },
            isError = !isPasswordValid,
            errorMessage = if (isPasswordValid) "" else stringResource(id = R.string.invalid_password),
            label = stringResource(id = R.string.prompt_password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            readOnly = screenState.isLoggingIn
        )
        Text(
            text = stringResource(id = R.string.action_go_sign_up),
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp)
                .conditional(!screenState.isLoggingIn) {
                    clickable { navigateToRegistration() }
                },
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {

            Button(
                onClick = {
                    login(email, password)
                },
                enabled = canSignIn && !screenState.isLoggingIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .conditional(screenState.isLoggingIn) {
                        drawAnimatedBorder(
                            2.dp,
                            CircleShape,
                            { Brush.sweepGradient(animationColors) },
                            2000
                        )
                    }

            ) {
                Text(
                    text = stringResource(id = R.string.action_sign_in)
                )
            }
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    var loginState by remember { mutableStateOf(LoginState()) }
    EasyStudyTheme {
        LoginScreen(
            loginState,
            { _, _ -> loginState = loginState.copy(isLoggingIn = true) },
            { },
            { email -> Patterns.EMAIL_ADDRESS.matcher(email).matches() },
            { password -> password.length in 6..20 }
        )
    }
}